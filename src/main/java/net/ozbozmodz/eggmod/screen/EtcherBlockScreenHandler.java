package net.ozbozmodz.eggmod.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.ozbozmodz.eggmod.entities.EtcherBlockEntity;
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

        // Add inventory slots, at the correct coords on the screen
        this.addSlot(new Slot(inventory, 0 , 0, 50));
        this.addSlot(new Slot(inventory, 1 , 50, 50));
        this.addSlot(new Slot(inventory, 2 , 90, 50));
        this.addSlot(new Slot(inventory, 3 , 50, 0));

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

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
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
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

}
