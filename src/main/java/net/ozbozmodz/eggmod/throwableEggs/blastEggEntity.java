package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class blastEggEntity extends customEggEntity{

    public blastEggEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

     @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(null, this.getX(), this.getY(), this.getZ(), 2, World.ExplosionSourceType.TRIGGER);
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }
    
}
