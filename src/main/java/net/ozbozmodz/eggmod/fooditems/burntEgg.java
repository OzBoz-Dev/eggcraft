package net.ozbozmodz.eggmod.fooditems;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class burntEgg extends Item{

    public burntEgg(Settings settings) {
        super(settings);
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Text> tooltip, TooltipType type){
        tooltip.add(Text.translatable("item.eggmod.burnt_egg.tooltip").formatted(Formatting.ITALIC));
        super.appendTooltip(itemStack, tooltipContext, tooltip, type);
    }
    //TODO When smelted, item should explode
}
