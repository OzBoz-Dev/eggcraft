package net.ozbozmodz.eggmod.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.List;

public class EtcherDisplay extends BasicDisplay {
    public EtcherDisplay() {
        super(List.of(tagToIngredient(RegisterAll.CUSTOM_EGGS)), List.of(tagToIngredient(RegisterAll.EGG_TEMPLATES)));

    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return EtcherCategory.ETCHER_DISPLAY_CATEGORY_IDENTIFIER;
    }

    private static EntryIngredient tagToIngredient(TagKey<Item> tag){
        return EntryIngredient.of(
                Registries.ITEM.getEntryList(tag)
                        .orElseThrow()
                        .stream()
                        .map(itemRegistryEntry -> EntryStacks.of(itemRegistryEntry.value()))
                        .toList()
        );
    }
}
