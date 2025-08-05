package net.ozbozmodz.eggmod.entities;

import com.google.common.base.Predicates;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.screen.ExperienceCatcherScreenHandler;
import net.ozbozmodz.eggmod.throwableEggs.ExperienceEggItem;
import net.ozbozmodz.eggmod.util.RegisterAll;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExperienceCatcherEntity extends BlockEntity implements ImplementedInventory, SidedInventory, ExtendedScreenHandlerFactory<BlockPos> {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public static final int SLOT = 0;

    // Property Delegate helps sync client and server
    protected final PropertyDelegate propertyDelegate;
    public int experience = 0;
    public int maxExperience = 2000;

    // Property delegate populated
    public ExperienceCatcherEntity(BlockPos pos, BlockState state) {
        super(RegisterAll.EXPERIENCE_CATCHER_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> ExperienceCatcherEntity.this.experience;
                    case 1 -> ExperienceCatcherEntity.this.maxExperience;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0 -> ExperienceCatcherEntity.this.experience = value;
                    case 1 -> ExperienceCatcherEntity.this.maxExperience = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;
        // If there is an experience egg item in the slot, and if it's not full
        if (!getStack(SLOT).isEmpty() && getStack(SLOT).isOf(RegisterAll.EXPERIENCE_EGG_ITEM)) {
            ItemStack stack = getStack(SLOT);
            if (ExperienceEggItem.getExperience(stack) < ExperienceEggItem.maxExperience && this.experience > 0) {
                ExperienceEggItem.addExp(1, stack);
                this.experience--;
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.05f, 2.0f);
                if (ExperienceEggItem.getExperience(stack) == ExperienceEggItem.maxExperience)
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 0.5f, 1.5f);
                markDirty(world, pos, state);
                world.updateListeners(pos, state, state, 0);
            }
        }
        // At the same time, if there are nearby experience orbs to collect, suck them in
        if (this.experience < this.maxExperience && !world.isReceivingRedstonePower(pos)) {
            Box box = new Box(pos.getX() - 10, pos.getY() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getY() + 10, pos.getZ() + 10);
            List<Entity> nearbyOrbs = world.getOtherEntities(null, box, Predicates.instanceOf(ExperienceOrbEntity.class));
            for (Entity e : nearbyOrbs) {
                collectOrb((ExperienceOrbEntity)e, world);
            }
            markDirty(world, pos, state);
            world.updateListeners(pos, state, state, 0);
        }
    }

    public void collectOrb(ExperienceOrbEntity e, World world){
        if (this.experience < this.maxExperience) {
            Vec3d moveTo = this.getPos().toCenterPos().add(0,0.5,0).subtract(e.getPos()).multiply(0.03f);
            e.addVelocity(moveTo);
            if (e.squaredDistanceTo(this.getPos().toCenterPos()) < 1) {
                this.experience += e.getExperienceAmount();
                e.discard();
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.1f, 1.0f + (Random.create().nextBetween(-1, 1) * 0.2f));
                if (this.experience > maxExperience) {
                    world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), maxExperience - this.experience));
                    this.experience = maxExperience;
                }
            }
        }
    }

    // Required Methods for functionality
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("experience_catcher.experience", experience);
        nbt.putInt("experience_catcher.maxExperience", maxExperience);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        experience = nbt.getInt("experience_catcher.experience");
        maxExperience = nbt.getInt("experience_catcher.maxExperience");
        super.readNbt(nbt, registryLookup);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.eggmod.experience_catcher");
    }

    @Override
    @Nullable
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ExperienceCatcherScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    // Sided Inventory Methods

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return stack.isOf(RegisterAll.EXPERIENCE_EGG_ITEM);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return stack.isOf(RegisterAll.EXPERIENCE_EGG_ITEM) && (ExperienceEggItem.getExperience(stack) == ExperienceEggItem.maxExperience);
    }
}
