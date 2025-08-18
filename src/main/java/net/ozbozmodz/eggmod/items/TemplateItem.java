package net.ozbozmodz.eggmod.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.ozbozmodz.eggmod.util.EggHelper;

import java.util.List;

public class TemplateItem extends Item {

    public TemplateItem(Settings settings) {
        super(settings.maxCount(1));
    }

    public String getType(){
        return Registries.ITEM.getId(this).getPath().replaceFirst("templates/", "").replace("_template", "");
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Item item = EggHelper.getCurrentOutputItem(stack.getItem());
        if (item != null)
            tooltip.add(Text.literal("Creates " + stack.getMaxDamage() + " ").append(Text.translatable(item.getTranslationKey())).formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
