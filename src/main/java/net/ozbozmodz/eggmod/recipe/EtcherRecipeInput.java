package net.ozbozmodz.eggmod.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record EtcherRecipeInput(ItemStack template, ItemStack serum, ItemStack egg) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        ItemStack result;
        switch(slot){
            case 0 -> result = this.template;
            case 1 -> result = this.serum;
            case 2 -> result = this.egg;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + slot);
        }
        return result;
    }

    @Override
    public int getSize() {
        return 3;
    }
}
