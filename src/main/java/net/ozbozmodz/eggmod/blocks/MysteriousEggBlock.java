package net.ozbozmodz.eggmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.ozbozmodz.eggmod.util.EggHelper;

import java.util.List;

public class MysteriousEggBlock extends Block {

    public MysteriousEggBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ModVoxelShapes.MYSTERIOUS_EGG_SHAPE;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        EggHelper.appendTooltip(stack, context, tooltip, options, "item.eggmod.mysterious_egg.tooltip");
        super.appendTooltip(stack, context, tooltip, options);
    }
}
