package net.ozbozmodz.eggmod.armor;

import net.minecraft.client.item.TooltipType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.ozbozmodz.eggmod.util.EggHelper;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.List;

public class EggshellArmorItem extends ArmorItem {
    public EggshellArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    // Custom sound since you can't change pitch over here, but it's just the anvil landing sound
    @Override
    public SoundEvent getBreakSound() {
        return RegisterAll.EGGSHELL_ARMOR_BREAK;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        EggHelper.appendTooltip(stack, context, tooltip, type, "item.eggmod.eggshell_armor.tooltip");
    }
}
