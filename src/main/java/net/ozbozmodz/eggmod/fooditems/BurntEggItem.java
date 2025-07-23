package net.ozbozmodz.eggmod.fooditems;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BurntEggItem extends Item{

    public BurntEggItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Text> tooltip, TooltipType type){
        tooltip.add(Text.translatable("item.eggmod.burnt_egg.tooltip").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
        super.appendTooltip(itemStack, tooltipContext, tooltip, type);
    }
}
