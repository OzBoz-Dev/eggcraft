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

public class DiamondEggEntity extends IronEggEntity {

    public DiamondEggEntity(World world, LivingEntity owner) {
        super(world, owner);
        spawnPos = owner.getPos();
        spawnRecorded = true;
    }

    public DiamondEggEntity(EntityType<? extends CustomEggEntity> damageEggEntityEntityType, World world) {
        super(damageEggEntityEntityType, world);
        spawnRecorded = false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();
        float baseDmg = 9.0F;
        double dist = Vector3d.distanceSquared(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), this.getX(), this.getY(), this.getZ());

        if (this.getOwner() != null) {
            entityHitResult.getEntity().damage(world.getDamageSources().playerAttack((PlayerEntity) this.getOwner()), (float) (baseDmg + (dist / 15)));
        }
        else {
            entityHitResult.getEntity().damage(world.getDamageSources().generic(), (float) (baseDmg + (dist/15)));
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

