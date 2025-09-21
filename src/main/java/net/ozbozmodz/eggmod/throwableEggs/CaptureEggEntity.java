package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class CaptureEggEntity extends CustomEggEntity{
    protected Entity captive;

    public CaptureEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        World world = getWorld();
        if (world.isClient()) return;
        // If we hit an entity and have no captured entity onboard, capture it and drop
        if (captive == null && hitResult.getType().equals(HitResult.Type.ENTITY)) {
            Entity e = ((EntityHitResult)hitResult).getEntity();
            if (!(e instanceof Monster) || e instanceof Tameable){
                ItemStack stack = new ItemStack(RegisterAll.CAPTURE_EGG_ITEM, 1);
                NbtCompound entityData = new NbtCompound();
                if (e.saveSelfNbt(entityData)) {
                    stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(entityData));
                    e.remove(RemovalReason.KILLED);
                    world.spawnEntity(new ItemEntity(world, this.getX(), this.getY(), this.getZ(), stack));
                    world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    ((ServerWorld) world).spawnParticles(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0, 0, 0, 0, 0);
                    super.onCollision(hitResult);
                    this.discard();
                    return;
                }
            }
        }
        // Otherwise, if we have a captive entity, spawn it
        else if (captive != null){
            captive.refreshPositionAndAngles(this.getBlockPos(), 0, 0);
            world.spawnEntity(captive);
            world.playSound(null, this.getBlockPos(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            ((ServerWorld)world).spawnParticles(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0,0,0,0,0);
        }
        // Recycle the item after, always
        world.spawnEntity(new ItemEntity(world, this.getX(), this.getY(), this.getZ(), new ItemStack(RegisterAll.CAPTURE_EGG_ITEM, 1)));
        super.onCollision(hitResult);
    }

    public void setCaptured(Entity captive) {
        this.captive = captive;
    }
}
