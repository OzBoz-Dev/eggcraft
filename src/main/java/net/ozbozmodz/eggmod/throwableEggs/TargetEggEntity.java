package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TargetEggEntity extends CustomEggEntity{

    public TargetEggEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    public TargetEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
    }
}
