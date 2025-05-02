package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Random;

public class blastEggEntity extends customEggEntity{

    public blastEggEntity(EntityType<? extends customEggEntity> entityType, World world) {
        super(entityType, world);
    }

    public blastEggEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        World world = this.getWorld();
        world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 3, World.ExplosionSourceType.TRIGGER);
        world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        Random r = new Random();
        if (!world.isClient()) {
            for (int i = 0; i < 16; i++) {
                ((ServerWorld)world).spawnParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 1, Math.cos(i * 30) * r.nextDouble(), 1.0f, Math.sin(i * 30) * r.nextDouble(), 0.2F);
            }
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient()) {
            ServerWorld world = (ServerWorld) this.getWorld();
            world.spawnParticles(ParticleTypes.SMALL_FLAME, this.getX(), this.getY() + 0.5F, this.getZ(), 1, 0, 0, 0, 0.1F);
        }
    }
}
