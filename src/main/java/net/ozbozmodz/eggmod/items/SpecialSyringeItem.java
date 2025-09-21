package net.ozbozmodz.eggmod.items;

import java.util.List;
import java.util.Random;

import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.EggHelper;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class SpecialSyringeItem extends Item{
    BlockPos eggPos;

    public SpecialSyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 100;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient()){
            if (world.getBlockState(eggPos).isOf(RegisterAll.MYSTERIOUS_EGG_BLOCK)){
                stack.decrement(1);
                user.dropItem(RegisterAll.ENDER_SERUM_ITEM);
                world.breakBlock(eggPos, false, user);
            }
        }
        BlockPos pos = user.getBlockPos();
        world.playSound((PlayerEntity)user, pos, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.BLOCKS, 1.0f, 0.7f);
        return stack;
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world instanceof ServerWorld) {
            Random r = new Random(remainingUseTicks);
            ((ServerWorld)world).spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getY(), user.getZ(),10, remainingUseTicks * Math.cos(remainingUseTicks * 30) * r.nextDouble(), 1.0f, remainingUseTicks * Math.sin(remainingUseTicks * 30) * r.nextDouble(), 1);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (world.getBlockState(pos).isOf(RegisterAll.MYSTERIOUS_EGG_BLOCK) && player != null){
            eggPos = pos;
            return ItemUsage.consumeHeldItem(world, player, player.getActiveHand()).getResult();
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        EggHelper.appendTooltip(stack, context, tooltip, type, "item.eggmod.special_syringe.tooltip");
        super.appendTooltip(stack, context, tooltip, type);
    }
}