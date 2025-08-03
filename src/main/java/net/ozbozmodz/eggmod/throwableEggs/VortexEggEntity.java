package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.*;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.ArrayList;
import java.util.List;

public class VortexEggEntity extends PersistentEggEntity{
    private List<Entity> targets;

    public VortexEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
        targets = new ArrayList<>();
        elapsedTicks = 0;
        active = false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        active = true;
        this.setVelocity(0, 0, 0);
        this.setNoGravity(true);
        this.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
        World world = this.getWorld();
        populateLists(world);
        this.setInvisible(true);
    }

    protected void populateLists(World world){
        BlockPos center = this.getBlockPos();
        List<Entity> nearbyEntities = world.getOtherEntities(this,
                new Box(center.getX() - 20,center.getY() - 10, center.getZ() - 20, center.getX() + 20, center.getY() + 10, center.getZ() + 20));
        targets.addAll(nearbyEntities);
    }

    protected void pullEntities(){
        showParticles();
        for (Entity e : targets) {
            Vec3d toCenter = e.getPos().subtract(this.getPos());
            if (e.getBlockPos().equals(this.getBlockPos())) continue;
            double dx = toCenter.x;
            double dz = toCenter.z;
            double pull = e.squaredDistanceTo(this.getX(), e.getY(), this.getZ()) > 2 ? 0.4 : 0.0;
            double spin = 0.2;
            double lift = e.squaredDistanceTo(this.getX(), e.getY(), this.getZ()) > 2 ? 0.04 : -0.04;
            double distance = Math.sqrt(dx * dx + dz * dz);
            double tangentialX = -dz / distance;
            double tangentialZ = dx / distance;
            double vx = (-dx / distance) * pull + tangentialX * spin;
            double vz = (-dz / distance) * pull + tangentialZ * spin;
            e.setVelocity(vx, lift, vz);
        }
    }

    protected void showParticles(){
        if (!this.getWorld().isClient()){
            ServerWorld world = (ServerWorld) this.getWorld();
            double radius = 3.0;
            double yOffset = -1.0;

            for (int i = 0; i < 20; i++) {
                double angle = 2 * Math.PI * i / 20 + (elapsedTicks * 0.1);
                double x = this.getX() + radius * Math.cos(angle);
                double z = this.getZ() + radius * Math.sin(angle);
                double y = this.getY() + yOffset;

                yOffset += 0.07;

                Vec3d motion = new Vec3d(-Math.sin(angle) * 0.05, 0.1, Math.cos(angle) * 0.05);
                world.spawnParticles(ParticleTypes.CLOUD,x,y,z,0,motion.x,motion.y,motion.z,1);
            }
        }
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        if (!active)
            super.tick();
        else if (!world.isClient()) {
            if (elapsedTicks >= 200){
                active = false;
                elapsedTicks = 0;
                ((ServerWorld)world).spawnParticles(ParticleTypes.GUST_EMITTER_LARGE, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                world.playSound(this, this.getBlockPos(), RegisterAll.VORTEX_LAUNCH, SoundCategory.HOSTILE, 3.0f, 1.0f);
                populateLists(world);
                for (Entity e : targets){
                    if (e.distanceTo(this) < 5){
                        e.setVelocity(0, 2, 0);
                    }
                }
                world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
                this.discard();
                return;
            }
            else {
                populateLists(world);
                pullEntities();
                if(elapsedTicks % 6 == 0) world.playSound(this, this.getBlockPos(), RegisterAll.VORTEX_AMBIENT, SoundCategory.BLOCKS, 3.0f, 1);
            }
            elapsedTicks++;
        }
    }
}
