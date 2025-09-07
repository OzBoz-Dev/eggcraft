package net.ozbozmodz.eggmod.throwableEggs;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.Comparator;
import java.util.List;

public class TargetEggEntity extends CustomEggEntity{

    public TargetEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        World world = this.getWorld();
        if (world.isClient()) return;
        // If we get a direct hit on the entity, just apply the effect on it (provided it's living)
        if (hitResult.getType().equals(HitResult.Type.ENTITY)){
            Entity entity = ((EntityHitResult)hitResult).getEntity();
            if (entity instanceof LivingEntity){
                applyEffect((LivingEntity) entity);
            }
            super.onCollision(hitResult);
        }
        // If the egg misses, then find the nearest livingEntity, if there is one
        else {
            BlockPos center = this.getBlockPos();
            Box bound = Box.of(center.toCenterPos(), 20, 20, 20);
            List<? extends Entity> nearbyEntities = world.getOtherEntities(this, bound, Predicates.instanceOf(LivingEntity.class));
            if (!nearbyEntities.isEmpty()) {
                // Find the nearest one, if it exists, then apply to that one
                @SuppressWarnings("unchecked")
                LivingEntity target = world.getClosestEntity((List<? extends LivingEntity>) nearbyEntities, TargetPredicate.DEFAULT, null, this.getX(), this.getY(), this.getZ());
                if (target != null && target != this.getOwner()) {
                    applyEffect(target);
                    }
            }
            super.onCollision(hitResult);
        }
    }

    protected void applyEffect(LivingEntity entity){
        // If the entity can't have it added (they are immune), add it anyway lol
        StatusEffectInstance effect1 = new StatusEffectInstance(StatusEffects.GLOWING, 600);
        StatusEffectInstance effect2 = new StatusEffectInstance(RegisterAll.LOCK_ON_EFFECT, 600);
        entity.addStatusEffect(effect1);
        if (!entity.addStatusEffect(effect2)) {
            entity.setStatusEffect(effect1, null);
            entity.setStatusEffect(effect2, null);
        }
        // Play a sound at the player/caster's position
        if(this.getOwner() != null) this.getWorld().playSound(this, this.getOwner().getBlockPos(), SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0f, 0.5f);
    }

    @Override
    public void tick() {
        Box box = Box.of(this.getPos(), 10, 10, 10);
        World world = this.getWorld();
        List<Entity> nearby = world.getOtherEntities(this, box, Predicates.instanceOf(LivingEntity.class));
        if (this.getOwner() != null) nearby.remove(this.getOwner());
        if (!nearby.isEmpty()) {
            nearby.sort(Comparator.comparingDouble(e -> e.distanceTo(this)));
            Entity target = nearby.get(0);
            this.setVelocity(target.getPos().subtract(this.getPos()));
        }
        super.tick();
    }
}
