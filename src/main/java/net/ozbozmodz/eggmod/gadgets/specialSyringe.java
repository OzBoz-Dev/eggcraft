package net.ozbozmodz.eggmod.gadgets;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterItems;

public class specialSyringe extends Item{

    public specialSyringe(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 64;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient()){
        stack.decrement(1);
        user.dropItem(RegisterItems.DRAGONSERUM);
        }
        BlockPos pos = user.getBlockPos();
        world.playSound((PlayerEntity)user, pos, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.BLOCKS, 1.0f, 0.7f);
        return stack;
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        Random r = new Random(remainingUseTicks);
        for (int i = 0; i < (64-remainingUseTicks)/2; i++){
            world.addParticle(ParticleTypes.PORTAL, user.getX(), user.getY(), user.getZ(), i*Math.cos(i*30)*r.nextDouble(), 1.0f, i*Math.sin(i*30)*r.nextDouble());
        }
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hr = user.raycast(4.5, 0.0f, true);
        BlockHitResult bhr;
        if (hr.getType() == HitResult.Type.BLOCK) {
            bhr = (BlockHitResult) hr;
            BlockPos pos = new BlockPos(bhr.getBlockPos());
            if (world.getBlockState(pos).getBlock() == Blocks.DRAGON_EGG) {
                world.playSound(user, pos, SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.BLOCKS, 1.0f, 2.0f);
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
        }
        return super.use(world, user, hand);
    }
}