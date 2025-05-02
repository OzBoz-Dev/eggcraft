package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.joml.Random;
import org.joml.Vector3d;

public class diamondEggEntity extends ironEggEntity {

    public diamondEggEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    public diamondEggEntity(EntityType<? extends customEggEntity> damageEggEntityEntityType, World world) {
        super(damageEggEntityEntityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();
        float baseDmg = 9.0F;

        if (this.getOwner() != null) {
            double dist = Vector3d.distanceSquared(this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(), this.getX(), this.getY(), this.getZ());
            entityHitResult.getEntity().damage(world.getDamageSources().playerAttack((PlayerEntity) this.getOwner()), (float) (baseDmg + (dist / 15)));
        }
        else {
            entityHitResult.getEntity().damage(world.getDamageSources().generic(), baseDmg);
        }
        world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 1.0F, 2.0F);
        Random r = new Random();
        if (!world.isClient()) {
            for (int i = 0; i < 10; i++) {
                ((ServerWorld) world).spawnParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 1, Math.cos(i * 30) * r.nextFloat(), 1.0f, Math.sin(i * 30) * r.nextFloat(), 0.2F);
            }
        }
        world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        this.discard();
    }
}

