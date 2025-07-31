package net.ozbozmodz.eggmod.fooditems;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.ozbozmodz.eggmod.util.EggHelper;

public class BurntEggItem extends Item{

    public BurntEggItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Text> tooltip, TooltipType type){
        EggHelper.appendTooltip(itemStack, tooltipContext, tooltip, type, "item.eggmod.burnt_egg.tooltip");
        super.appendTooltip(itemStack, tooltipContext, tooltip, type);
    }
}
