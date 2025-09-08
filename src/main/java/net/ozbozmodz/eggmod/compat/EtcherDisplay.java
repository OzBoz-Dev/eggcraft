package net.ozbozmodz.eggmod.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.ozbozmodz.eggmod.recipe.EtcherRecipe;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.List;

public class EtcherDisplay extends BasicDisplay {
    public EtcherDisplay(RecipeEntry<EtcherRecipe> recipe) {
        super(List.of(EntryIngredients.ofIngredient(recipe.value().getIngredients().get(0))),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.value().getResult(null)))));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return EtcherCategory.ETCHER_DISPLAY_CATEGORY_IDENTIFIER;
    }
}
