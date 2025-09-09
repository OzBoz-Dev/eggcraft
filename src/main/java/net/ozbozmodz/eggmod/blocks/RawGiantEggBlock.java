package net.ozbozmodz.eggmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.ozbozmodz.eggmod.util.EggHelper;

import java.util.List;

public class RawGiantEggBlock extends Block {
    public RawGiantEggBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ModVoxelShapes.GIANT_EGG_SHAPES[0];
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        EggHelper.appendTooltip(stack, context, tooltip, options, "item.eggmod.raw_giant_egg.tooltip");
        super.appendTooltip(stack, context, tooltip, options);
    }
}
