package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HermesEggEntity extends CustomEggEntity{

    public HermesEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        // Give the passenger slow fall as they dismount
        passenger.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 0));
        this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
        this.discard();
        return super.updatePassengerForDismount(passenger);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return (LivingEntity) this.getOwner();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        // Dismount the passenger on collision to give them slow fall as well
        if (this.getControllingPassenger() != null) this.getControllingPassenger().stopRiding();
        super.onCollision(hitResult);
    }
}
