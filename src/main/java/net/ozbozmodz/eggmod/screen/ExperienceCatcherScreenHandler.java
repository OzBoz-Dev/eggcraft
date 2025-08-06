package net.ozbozmodz.eggmod.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.ozbozmodz.eggmod.entities.ExperienceCatcherEntity;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class ExperienceCatcherScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final ExperienceCatcherEntity blockEntity;

    // Called just in case we don't have a property delegate or need to get the entity manually
    public ExperienceCatcherScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos){
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(4));
    }

    public ExperienceCatcherScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(RegisterAll.EXPERIENCE_CATCHER_SCREEN_HANDLER, syncId);

        this.inventory = (Inventory) blockEntity;
        this.blockEntity = ((ExperienceCatcherEntity) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;

        // Add inventory slots, at the correct coords on the screen;
        this.addSlot(new customSlot(inventory, 0 , 98, 73));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(arrayPropertyDelegate);
    }

    // For EXP bar
    public int getEnergyBar(){
        // This is for the energy bar
        int experience = this.propertyDelegate.get(0);
        int maxExperience = this.propertyDelegate.get(1);
        int barSize = 58;

        return maxExperience != 0 && experience != 0 ? experience * barSize / maxExperience : 0;
    }

    // For the pipe at the bottom
    public int getPipeWidth(){
        int experience = getExp();
        int maxExperience = 30;
        int pipeWidth = 37;
        if (experience == 0) return 0;
        return  experience <= maxExperience ? experience * pipeWidth / maxExperience : 37;
    }

    public int getExp(){
        return this.propertyDelegate.get(0);
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
            if (this.getIndex() == 0) return stack.isOf(RegisterAll.EXPERIENCE_EGG_ITEM);
            else return super.canInsert(stack);
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
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 104 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 162));
        }
    }
}
