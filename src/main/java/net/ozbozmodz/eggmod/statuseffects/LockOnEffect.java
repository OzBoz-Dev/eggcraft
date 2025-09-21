package net.ozbozmodz.eggmod.statuseffects;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.awt.*;
import java.util.List;

public class LockOnEffect extends StatusEffect {
    public float ticksElapsed;
    public LockOnEffect() {
        super(StatusEffectCategory.NEUTRAL, Color.HSBtoRGB(0, 100, 46));
        ticksElapsed = 0;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        // Show a particle above the targeted entity's head
        World world = entity.getWorld();
        if(ticksElapsed % 40 == 0 && !world.isClient()) {
            ((ServerWorld)world).spawnParticles(RegisterAll.LOCK_ON_PARTICLE, entity.getX(), entity.getY()+entity.getHeight()+0.5, entity.getZ(), 1, entity.getMovementDirection().getOffsetX(),0,entity.getMovementDirection().getOffsetZ(),0.5f);
            ticksElapsed = 0;
        }
        // Box where we will capture all nearby arrows
        Box bound = Box.of(entity.getPos(), 20,20,20);
        List<Entity> projectiles = world.getOtherEntities(entity, bound, Predicates.instanceOf(ProjectileEntity.class));
        if (!world.isClient()) {
            for (Entity e : projectiles) {
                // If we find a projectile entity in the air
                if (e instanceof ProjectileEntity p && p.distanceTo(entity) > 2) {
                    NbtCompound n = new NbtCompound();
                    p.writeNbt(n);
                    if (n.contains("inGround")){
                        if (n.getBoolean("inGround")) continue;
                    }
                    // Send it flying towards the target
                    Vec3d vecToTarget = new Vec3d(entity.getX() - p.getX(), entity.getY() + 1 - p.getY(), entity.getZ() - p.getZ());
                    ((ServerWorld) world).spawnParticles(ParticleTypes.CRIMSON_SPORE, p.getX(), p.getY(), p.getZ(), 1, 0, 0, 0, 0);
                    p.velocityDirty = true;
                    p.setVelocity(p.getVelocity().multiply(0.5));
                    p.setBodyYaw(p.getMovementDirection().asRotation());
                    // Use lerp and other things to make the movement curve seem natural
                    if (p.distanceTo(entity) > 5) p.addVelocity(p.getVelocity().lerp(vecToTarget, 0.5).multiply(0.2));
                    else p.addVelocity(p.getVelocity().lerp(vecToTarget, 0.5).multiply(0.5));
                    if (p.distanceTo(entity) < 3) p.setVelocity(p.getVelocity().multiply(2));
                    // Prevent odd behavior with tridents
                    if (p.distanceTo(entity) < 1) {
                        p.setOnGround(true);
                    }
                }
            }
            ticksElapsed++;
        }
        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }


}
