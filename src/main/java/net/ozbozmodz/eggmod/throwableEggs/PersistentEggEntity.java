package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class PersistentEggEntity extends CustomEggEntity{
    public boolean active;
    public int elapsedTicks;

    public PersistentEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean shouldSave() {
        return true;
    }

    // Save the activation state and elapsed ticks on exiting the world
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Active", active);
        nbt.putInt("ElapsedTicks", elapsedTicks);
    }

    // Reload to prevent it from losing behavior
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.active = nbt.getBoolean("Active");
        this.elapsedTicks = nbt.getInt("ElapsedTicks");
    }
}
