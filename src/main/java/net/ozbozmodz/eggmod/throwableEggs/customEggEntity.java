package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

/* Abstract egg entity that all our special ones will extend */
public abstract class customEggEntity extends SnowballEntity {

    public customEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    public customEggEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            for (int i = 0; i < 8; i++) {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(),
                        (this.random.nextFloat() - 0.5) * 0.08, (this.random.nextFloat() - 0.5) * 0.08, (this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }
}
