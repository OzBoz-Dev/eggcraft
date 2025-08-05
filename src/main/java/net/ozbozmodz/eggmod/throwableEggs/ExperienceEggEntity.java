package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class ExperienceEggEntity extends CustomEggEntity{
    protected int experience;

    public ExperienceEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        World world = this.getWorld();
        if (!world.isClient()){
            this.getWorld().syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, this.getBlockPos(), PotionContentsComponent.getColor(Potions.WATER));
            while (this.experience >= 20){
                world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), 20));
                this.experience -= 20;
            }
            if (this.experience > 0) world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.experience));

        }
        super.onCollision(hitResult);
    }

    public void setExperience(int experience){
        this.experience = experience;
    }
}
