package net.ozbozmodz.eggmod.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record EtcherRecipeInput(ItemStack template, ItemStack egg) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch(slot){
            case 0 -> this.template;
            case 1 -> this.egg;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int getSize() {
        return 2;
    }
}
