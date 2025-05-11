package net.ozbozmodz.eggmod.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.ozbozmodz.eggmod.entities.EtcherBlockEntity;
import net.ozbozmodz.eggmod.items.TemplateItem;
import net.ozbozmodz.eggmod.util.RegisterItems;

public class EtcherBlockScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final EtcherBlockEntity blockEntity;

    // Called just in case we don't have a property delegate or need to get the entity manually
    public EtcherBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos){
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(4));
    }

    public EtcherBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(RegisterItems.ETCHER_BLOCK_SCREEN_HANDLER, syncId);

        this.inventory = (Inventory) blockEntity;
        this.blockEntity = ((EtcherBlockEntity) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;

        // Add inventory slots, at the correct coords on the screen;
        this.addSlot(new customSlot(inventory, 0 , 26, 26));
        this.addSlot(new customSlot(inventory, 1 , 133, 26));
        this.addSlot(new customSlot(inventory, 2 , 80, 20));
        this.addSlot(new customSlot(inventory, 3 , 80, 66));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(arrayPropertyDelegate);
    }

    public boolean isCrafting(){
        // Property delegate stores progress in 0. So, if progress > 0, we are crafting
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledArrowProgress(){
        // This is for the progress arrow
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public int getEnergyBar(){
        // This is for the energy bar
        int energy = this.propertyDelegate.get(2);
        int maxEnergy = this.propertyDelegate.get(3);
        int barSize = 28;

        return maxEnergy != 0 && energy != 0 ? energy * barSize / maxEnergy : 0;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.canInsert(stack);
    }

    // Custom Slot to include logic preventing illegal insertions
    static class customSlot extends Slot{

        public customSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }
        // So players can't insert or quick move items that shouldn't be moved
        @Override
        public boolean canInsert(ItemStack stack) {
            return switch (this.getIndex()){
                case 0 -> stack.getItem() instanceof TemplateItem;
                case 1 -> stack.isOf(RegisterItems.ENDER_SERUM_ITEM);
                case 2 -> stack.isOf(Items.EGG);
                case 3 -> false;
                default -> super.canInsert(stack);
            };
        }
    }

    // Following three methods credit: Modding by Kaupenjoe
    // https://github.com/Tutorials-By-Kaupenjoe/

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        // Check our slot, and then quick move the item
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 96 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 154));
        }
    }
}
