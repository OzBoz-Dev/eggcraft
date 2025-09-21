package net.ozbozmodz.eggmod.items;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.ozbozmodz.eggmod.util.EggHelper;

import java.util.List;

public class EggshellItem extends BlockItem {
    public EggshellItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        EggHelper.appendTooltip(stack, context, tooltip, type, "item.eggmod.eggshell.tooltip");
        super.appendTooltip(stack, context, tooltip, type);
    }
}
