package net.ozbozmodz.eggmod.fooditems;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class burntEgg extends Item{

    public burntEgg(Settings settings) {
        super(settings);
    }

//    @Override
//    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
//        super.onCraftByPlayer(stack, world, player);
//        HitResult hr = player.raycast(4.5, 0.0f, true);
//        BlockHitResult bhr;
//        if (hr.getType() == HitResult.Type.BLOCK) {
//            bhr = (BlockHitResult) hr;
//            BlockPos pos = new BlockPos(bhr.getBlockPos());
//            if (world.getBlockState(pos).getBlock() == Blocks.FURNACE) {
//                world.createExplosion(null, pos.getX() , pos.getY(), pos.getZ(), 2, World.ExplosionSourceType.BLOCK);
//            }
//        }
//
//    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Text> tooltip, TooltipType type){
        tooltip.add(Text.translatable("item.eggmod.burnt_egg.tooltip").formatted(Formatting.ITALIC));
        super.appendTooltip(itemStack, tooltipContext, tooltip, type);
    }
    //TODO When smelted, item should explode
}
