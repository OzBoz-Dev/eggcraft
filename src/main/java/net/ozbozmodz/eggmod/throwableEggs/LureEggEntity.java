package net.ozbozmodz.eggmod.throwableEggs;

import com.google.common.base.Predicates;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class LureEggEntity extends PersistentEggEntity{
    List<PassiveEntity> scannedPassiveMobs;

    public LureEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
        active = false;
        elapsedTicks = 0;
        scannedPassiveMobs = new ArrayList<>();
        System.out.println("Constructed!");
    }



    @Override
    protected void onCollision(HitResult hitResult) {
        this.setVelocity(0, 0.1, 0, 0.5f, 0.1f);
        World world = this.getWorld();
        if (!world.isClient()) {
            populateList(world);
        }
        // Activate the egg's behavior
        active = true;
    }

    @Override
    public void tick() {
        if (!active) {
            super.tick();
        }
        else {
            // So it floats up when active
            if (elapsedTicks < 10){
                super.tick();
            }
            else {
                World world = this.getWorld();
                if (!world.isClient()) {
                    // If reloading world
                    if (scannedPassiveMobs.isEmpty()) populateList(world);
                    // Sound logic
                    if (elapsedTicks == 10) world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 0.5F, 0.4F);
                    if (elapsedTicks == 290) world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 0.5F, 1.0F);
                    this.setNoGravity(true);
                    this.setVelocity(0, 0, 0);
                    // Spawn particles while it ticks
                    Random r = Random.create();
                    if (elapsedTicks % 4 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.HEART, this.getX()+(r.nextBetween(-1,1)*0.5f), this.getY(), this.getZ()+(r.nextBetween(-1,1)*0.5f), 1, 0, -1, 0, 0.1f);
                    // Discard the lure egg when its time is up, with a sound and particles
                    if (elapsedTicks > 300) {
                        active = false;
                        elapsedTicks = 0;
                        ((ServerWorld)world).spawnParticles(ParticleTypes.HEART, this.getX()+(r.nextBetween(-1,1)*0.5f), this.getY(), this.getZ()+(r.nextBetween(-1,1)*0.5f), 10, r.nextBetween(-1,1), r.nextBetween(-1,1), r.nextBetween(-1,1), 0.5f);
                        // Stop passive mob navigation
                        for (PassiveEntity e : scannedPassiveMobs) {
                            e.getNavigation().stop();
                        }
                        this.discard();
                        world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
                        return;
                    }
                    else {
                        // Cause all the nearby passive mobs to navigate to and look at this egg
                        for (PassiveEntity e : scannedPassiveMobs) {
                            e.getLookControl().lookAt(this);
                            if (e instanceof VillagerEntity){
                                e.getNavigation().startMovingTo(this, 0.5f);
                            }
                            else e.getNavigation().startMovingTo(this, 1.0f);
                            if (elapsedTicks % 8 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, e.getX(), e.getY()+1, e.getZ(), 1, 0,0,0,0);
                    }

                    }
                }
            }
            // Elapsed ticks since activation
            elapsedTicks++;
        }
    }

    protected void populateList(World world){
        BlockPos center = this.getBlockPos();
        // Find nearby entities, filtered down to only passive mobs, and add them to a list
        List<Entity> nearbyEntities = world.getOtherEntities(this, Box.of(center.toCenterPos(), 60, 20, 60), Predicates.instanceOf(PassiveEntity.class));
        for (Entity e : nearbyEntities){
            if (e instanceof PassiveEntity){
                scannedPassiveMobs.add((PassiveEntity) e);
            }
        }
    }
}
