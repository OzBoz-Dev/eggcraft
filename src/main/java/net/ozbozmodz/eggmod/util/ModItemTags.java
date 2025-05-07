package net.ozbozmodz.eggmod.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {
    public static final TagKey<Item> EGGZOOKA_PROJECTILES = TagKey.of(RegistryKeys.ITEM, Identifier.of("eggmod", "eggzooka_projectiles"));
}
