package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.EggHelper;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class HermesEggItem extends CustomEggItem{
    public HermesEggItem(Item.Settings settings) {
        super(settings);
    }

    public void hermesLaunch(World world, LivingEntity user, ItemStack itemStack, int useTicks) {
        ourEgg.setItem(itemStack);
        ourEgg.setPos(user.getX(), user.getEyeY(), user.getZ());
        ourEgg.velocityDirty = true;
        ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, Math.min((float) useTicks/8, 3.0f), 1.0f);
        world.spawnEntity(ourEgg);
        if (ourEgg.getOwner() != null) user.startRiding(ourEgg);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(remainingUseTicks%4==0) {
            user.playSound(RegisterAll.VORTEX_AMBIENT, 2.0f, Math.min((float) (getMaxUseTime(stack) - remainingUseTicks) / 20, 1.0f));
            if (!world.isClient()) ((ServerWorld)world).spawnParticles(ParticleTypes.CLOUD, user.getX(), user.getEyeY(), user.getZ(), 1, Math.cos(remainingUseTicks), 0 , Math.sin(remainingUseTicks) ,0);
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 300;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof ServerPlayerEntity userP) {
            Criteria.CONSUME_ITEM.trigger(userP, stack);
            userP.incrementStat(Stats.USED.getOrCreateStat(this));
            EggHelper.setCooldowns(userP, getTypeString());
        }
        world.playSoundFromEntity(user, RegisterAll.HERMES_TAKEOFF, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (!world.isClient()){
            type = Registries.ITEM.getId(this).getPath();
            ourEgg = EggHelper.getType(type, world);
            if (ourEgg == null) return;
            ourEgg.setOwner(user);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!world.isClient) {
                Random r = Random.create();
                hermesLaunch(world, user, stack, getMaxUseTime(stack)-remainingUseTicks);
                ((ServerWorld) world).spawnParticles(ParticleTypes.CLOUD, user.getX(), user.getEyeY(), user.getZ(), 100, r.nextBetween(-1,1), r.nextBetween(-1,0), r.nextBetween(-1,1), 0.5);
            }

        }
        stack.decrementUnlessCreative(1, user);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
