package net.ozbozmodz.eggmod.entities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ozbozmodz.eggmod.blocks.EtcherBlock;
import net.ozbozmodz.eggmod.items.TemplateItem;
import net.ozbozmodz.eggmod.screen.EtcherBlockScreenHandler;
import net.ozbozmodz.eggmod.throwableEggs.CustomEggItem;
import net.ozbozmodz.eggmod.util.RegisterItems;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.World;

public class EtcherBlockEntity extends BlockEntity implements EtcherInventory, SidedInventory, ExtendedScreenHandlerFactory<BlockPos> {
    // 4 Slots: Template, Serum, Egg, Output
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private static final int TEMPLATE_SLOT = 0;
    private static final int SERUM_SLOT = 1;
    private static final int EGG_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;
    private Direction templateDirection;
    private float rotation;
    private float accel;

    // Property Delegate helps sync client and server
    protected final PropertyDelegate propertyDelegate;
    // 10 Seconds per craft
    private int progress = 0;
    private int maxProgress = 150;
    private int energy = 0;
    private int maxEnergy = 16;

    public EtcherBlockEntity(BlockPos pos, BlockState state) {
        super(RegisterItems.ETCHER_BLOCK_ENTITY, pos, state);
        // By default, template and eggs face the same way as the block
        this.templateDirection = state.get(Properties.HORIZONTAL_FACING);
        // This is to sync our client and server values for progress/energy when crafting
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> EtcherBlockEntity.this.progress;
                    case 1 -> EtcherBlockEntity.this.maxProgress;
                    case 2 -> EtcherBlockEntity.this.energy;
                    case 3 -> EtcherBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0 -> EtcherBlockEntity.this.progress = value;
                    case 1 -> EtcherBlockEntity.this.maxProgress = value;
                    case 2 -> EtcherBlockEntity.this.energy = value;
                    case 3 -> EtcherBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    // Calls on every tick and does our crafting block logic
    public void tick(World world, BlockPos pos, BlockState state){
        // Set energy. If we have an ender serum in the slot, empty it and fill energy to max
        if (!getStack(SERUM_SLOT).isEmpty() && energy == 0
                && getStack(SERUM_SLOT).isOf(RegisterItems.ENDER_SERUM_ITEM)){
            this.energy = maxEnergy;
            setStack(SERUM_SLOT, new ItemStack(RegisterItems.SPECIAL_SYRINGE_ITEM));
            world.setBlockState(pos, state.with(EtcherBlock.ENERGY, true));
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.2f);
            markDirty(world, pos, state);
            world.updateListeners(pos, state, state, 0);
        }

        // If energy is zero, change it back to unactivated state
        if (energy == 0 && world.getBlockState(pos).get(EtcherBlock.ENERGY)){
            world.setBlockState(pos, state.with(EtcherBlock.ENERGY, false));
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.2f);
            // Marking dirty and updating listeners ensures that client side renderers will catch the change
            markDirty(world, pos, state);
            world.updateListeners(pos, state, state, 0);
        }

        // If our recipe is valid
        if (hasRecipe() && energy > 0){
            // Increase progress and mark to client
            progress++;
            // We can now craft the item, reset progress and decrease energy
            if (this.progress >= this.maxProgress){
                craftItem();
                this.progress = 0;
                energy--;
            }
            markDirty(world, pos, state);
            world.updateListeners(pos, state, state, 0);
        }
        else {
            this.progress = 0;
            markDirty(world, pos, state);
            world.updateListeners(pos, state, state, 0);
        }
    }

    private void craftItem(){
        Item output = getCurrentOutputItem(getStack(TEMPLATE_SLOT).getItem());
        // Decrease the eggs
        this.getStack(EGG_SLOT).decrement(1);
        // Do durability damage to the template, and remove it if it falls to 0
        this.getStack(TEMPLATE_SLOT).setDamage(this.getStack(TEMPLATE_SLOT).getDamage()+1);
        if (this.getStack(TEMPLATE_SLOT).getDamage() == 16) this.getStack(TEMPLATE_SLOT).decrement(1);
        // Put the result in the output
        this.setStack(OUTPUT_SLOT, new ItemStack(output, this.getStack(OUTPUT_SLOT).getCount() + 1));
    }

    private boolean hasRecipe() {
        // Check if we have an egg in slot and a valid template item
        boolean validTemplate = getStack(TEMPLATE_SLOT).getItem() instanceof TemplateItem;
        boolean validEgg = getStack(EGG_SLOT).isOf(Items.EGG);
        boolean validRecipe = validTemplate && validEgg;
        Item output = null;
        // Make sure we can properly parse the template input to the corresponding egg output
        if (validRecipe) {
            output = getCurrentOutputItem(getStack(TEMPLATE_SLOT).getItem());
        }
        if (output == null) return false;

        // Does the output slot have an invalid or different egg item in it already?
        boolean validOutputItem = this.getStack(OUTPUT_SLOT).isEmpty()
                || this.getStack(OUTPUT_SLOT).isOf(output);
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();
        // If we can fit one more item in the output slot
        boolean validOutputAmount = maxCount >= currentCount + 1;
        return validRecipe && validOutputItem && validOutputAmount;
    }

    // Convert our template item to the corresponding output item
    public CustomEggItem getCurrentOutputItem(Item template){
        return switch(((TemplateItem)template).getType()){
            case "BLANK" -> null;
            case "BLASTEGG" -> RegisterItems.BLAST_EGG_ITEM;
            case "IRONEGG" -> RegisterItems.IRON_EGG_ITEM;
            case "DIAMONDEGG" -> RegisterItems.DIAMOND_EGG_ITEM;
            case "EXCAVATOREGG" -> RegisterItems.EXCAVATOR_EGG_ITEM;
            default -> null;
        };
    }


    // Required Methods for functionality

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("etcher_block.progress", progress);
        nbt.putInt("etcher_block.max_progress", maxProgress);
        nbt.putInt("etcher_block.energy", energy);
        nbt.putInt("etcher_block.max_energy", maxEnergy);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("etcher_block.progress");
        maxProgress = nbt.getInt("etcher_block.max_progress");
        energy = nbt.getInt("etcher_block.energy");
        maxEnergy = nbt.getInt("etcher_block.max_energy");
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
        return Text.translatable("block.eggmod.etcher_block");
    }

    @Override
    @Nullable
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new EtcherBlockScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    // Required for sided inventory

    // 4 slots to interact with hoppers
    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{0, 1, 2, 3};
    }

    // Logic for insertion into each slot
    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return switch (slot){
            case TEMPLATE_SLOT -> stack.getItem() instanceof TemplateItem;
            case EGG_SLOT -> stack.isOf(Items.EGG);
            case SERUM_SLOT -> stack.isOf(RegisterItems.ENDER_SERUM_ITEM);
            default -> false;
        };
    }

    // Can only extract from output slot, and remove empty syringes from the serum slot as well
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == OUTPUT_SLOT || (slot == SERUM_SLOT && stack.isOf(RegisterItems.SPECIAL_SYRINGE_ITEM));
    }

    public Direction getTemplateDirection() {
        return templateDirection;
    }

    // Rotate the rendered egg item, faster with more progress, and gradually slow it down
    public float getEggRotation(){
        if (progress == 0 && !hasRecipe()) return this.getTemplateDirection().asRotation();
        if (progress >= 130){
            accel -= 0.1f;
        }
//        else if (progress > 145){
//            accel = 0.01f;
//        }
        else accel = 0.1f * progress;
        rotation += accel;
        if (rotation >= 360) rotation = 0;
        return rotation;
    }
}
