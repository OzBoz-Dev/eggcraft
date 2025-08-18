package net.ozbozmodz.eggmod.statuseffects;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class RepelEffect extends StatusEffect {
    public float ticksElapsed;
    public RepelEffect() {
        super(StatusEffectCategory.BENEFICIAL, Color.HSBtoRGB(58, 72, 30));
        ticksElapsed = 0;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        // Box where we will capture all nearby mobs
        double rD = amplifier * 12;
        Box bound = Box.of(entity.getPos(), rD, rD, rD);
        List<Entity> nearbyMobs = world.getOtherEntities(entity, bound, Predicates.instanceOf(LivingEntity.class));
        for (Entity target : nearbyMobs){
            // If we find any living entities nearby
            if (target instanceof PathAwareEntity pathAwareEntity){
                // Send it running away from the player
                Vec3d awayFromMe = new Vec3d((target.getX() - entity.getX())*2, target.getY()+1 - entity.getY(), (target.getZ() - entity.getZ())*2);
                Random r = new Random();
                if (ticksElapsed % 4 == 0 && !world.isClient()) ((ServerWorld)world).spawnParticles(ParticleTypes.FALLING_WATER, target.getX()+r.nextFloat(-0.3f, 0.3f), target.getY()+target.getHeight()+r.nextFloat(-0.3f, 0), target.getZ()+r.nextFloat(-0.3f, 0.3f), 1, 0,0,0,0);
                // Prevent it from going into attack states where it may not flee
                pathAwareEntity.setAttacking(false);
                pathAwareEntity.stopUsingItem();
                pathAwareEntity.setTarget(null);
                pathAwareEntity.stopUsingItem();
                pathAwareEntity.getNavigation().startMovingTo(entity.getX() + awayFromMe.getX(), entity.getY()+awayFromMe.getY(), entity.getZ()+awayFromMe.getZ(), amplifier);
            }
        }
        if (!world.isClient()) ticksElapsed++;
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}