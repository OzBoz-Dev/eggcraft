package net.ozbozmodz.eggmod.util;

import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import javax.swing.text.html.HTML;

public class modItemTags {
    public static final TagKey<Item> EGGZOOKA_PROJECTILES = TagKey.of(RegistryKeys.ITEM, Identifier.of("eggmod", "eggzooka_projectiles"));
}
