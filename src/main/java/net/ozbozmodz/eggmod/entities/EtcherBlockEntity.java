package net.ozbozmodz.eggmod.entities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
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
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.ozbozmodz.eggmod.screen.EtcherBlockScreenHandler;
import net.ozbozmodz.eggmod.throwableEggs.CustomEggItem;
import net.ozbozmodz.eggmod.util.RegisterItems;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.World;

public class EtcherBlockEntity extends BlockEntity implements EtcherInventory, ExtendedScreenHandlerFactory<BlockPos> {
    // 4 Slots: Template, Serum, Egg, Output
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private static final int TEMPLATE_SLOT = 0;
    private static final int SERUM_SLOT = 1;
    private static final int EGG_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    // Property Delegate helps sync client and server
    protected final PropertyDelegate propertyDelegate;
    // 10 Seconds per craft
    private int progress = 0;
    private int maxProgress = 40;

    public EtcherBlockEntity(BlockPos pos, BlockState state) {
        super(RegisterItems.ETCHER_BLOCK_ENTITY, pos, state);
        // This is to sync our client and server values for progress when crafting
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> EtcherBlockEntity.this.progress;
                    case 1 -> EtcherBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0 -> EtcherBlockEntity.this.progress = value;
                    case 1 -> EtcherBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    // Calls on every tick and does our crafting block logic
    public void tick(World world, BlockPos pos, BlockState state){
        // If our recipe is valid
        if (hasRecipe()){
            // Increase progress and mark to client
            progress++;
            markDirty(world, pos, state);

            // We can now craft the item
            if (this.progress >= this.maxProgress){
                craftItem();
                this.progress = 0;
            }
        }
        else {
            this.progress = 0;
        }
    }

    private void craftItem(){
        Item output = getCurrentOutputItem();
        this.getStack(EGG_SLOT).decrement(1);
        this.setStack(OUTPUT_SLOT, new ItemStack(output, this.getStack(OUTPUT_SLOT).getCount() + 1));
    }

    private boolean hasRecipe() {
        boolean validTemplate = getStack(TEMPLATE_SLOT).isOf(Items.PAPER);
        boolean validSerum = getStack(SERUM_SLOT).isOf(RegisterItems.ENDER_SERUM_ITEM);
        boolean validEgg = getStack(EGG_SLOT).isOf(Items.EGG);
        boolean validRecipe = validTemplate && validEgg && validSerum;
        Item output = getCurrentOutputItem();

        // Does the slot have an invalid or different egg item in it already?
        boolean validOutputItem = this.getStack(OUTPUT_SLOT).isEmpty()
                || this.getStack(OUTPUT_SLOT).isOf(output);
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();
        // If we can fit one more item in the output slot
        boolean validOutputAmount = maxCount >= currentCount + 1;
        return validRecipe && validOutputItem && validOutputAmount;
    }

    // TODO Make template items for each egg, and set the output accordingly
    private CustomEggItem getCurrentOutputItem(){
        return RegisterItems.BLAST_EGG_ITEM;
    }


    // Required Methods for functionality

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("etcher_block.progress", progress);
        nbt.putInt("etcher_block.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("etcher_block.progress");
        maxProgress = nbt.getInt("etcher_block.max_progress");
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

}
