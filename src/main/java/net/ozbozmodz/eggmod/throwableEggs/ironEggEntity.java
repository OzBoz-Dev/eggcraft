package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Random;
import org.joml.Vector3d;
/* Deal damage on landing */
public class ironEggEntity extends customEggEntity {
    Vec3d spawnPos;
    boolean spawnRecorded;

    public ironEggEntity(World world, LivingEntity owner) {
        super(world, owner);
        spawnPos = owner.getPos();
        spawnRecorded = true;
    }

    public ironEggEntity(EntityType<? extends customEggEntity> damageEggEntityEntityType, World world) {
        super(damageEggEntityEntityType, world);
        spawnRecorded = false;
    }

    @Override
    public void tick() {
        if (!spawnRecorded){
            this.spawnPos = this.getPos();
            spawnRecorded = true;
        }
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();
        float baseDmg = 6.0F;
        double dist = Vector3d.distanceSquared(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), this.getX(), this.getY(), this.getZ());
        // Do extra damage depending on how far the thrower is from it. Else just only do base dmg
        if (this.getOwner() != null) {
                       entityHitResult.getEntity().damage(world.getDamageSources().playerAttack((PlayerEntity) this.getOwner()), (float) (baseDmg + (dist / 15)));
        }
        else {
            entityHitResult.getEntity().damage(world.getDamageSources().generic(), (float) (baseDmg + (dist/15)));
        }
        // Play a sound, and add particles around the hit mob
        world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 1.0F, 2.0F);
        Random r = new Random();
        if (!world.isClient()) {
            for (int i = 0; i < 10; i++) {
                ((ServerWorld) world).spawnParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 1, Math.cos(i * 30) * r.nextFloat(), 1.0f, Math.sin(i * 30) * r.nextFloat(), 0.2F);
            }
        }
        // Clean up
        world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        // Drop the egg if it hits a block rather than entity
        super.onBlockHit(blockHitResult);
        World world = this.getWorld();
        world.spawnEntity(new ItemEntity(world, this.getX(), this.getY(), this.getZ(), this.getStack()));
        world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        this.discard();
    }
}

