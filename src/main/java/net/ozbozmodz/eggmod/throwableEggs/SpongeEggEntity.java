package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.block.*;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class SpongeEggEntity extends CustomEggEntity{

    public SpongeEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        World world = this.getWorld();
        if (!world.isClient()) {
            // Find all nearby blocks in a cube
            BlockPos center = new BlockPos((int) hitResult.getPos().getX(), (int) hitResult.getPos().getY(), (int) hitResult.getPos().getZ());
            for (int x = -6; x <= 6; x++) {
                for (int y = -6; y <= 6; y++) {
                    for (int z = -6; z <= 6; z++) {
                        BlockPos check = new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z);
                        BlockState state = world.getBlockState(check);
                        Random r = new Random();
                        // If it's a fluid source, replace with air, if it drains, then try to drain it
                        if (isFluid(state)) {
                            // Spawn a particle when destroying a fluid block
                            ((ServerWorld)world).spawnParticles(ParticleTypes.POOF, check.getX(), check.getY(), check.getZ(), 1, Math.cos(x * 30) * r.nextDouble(), 1.0f, Math.sin(z * 30) * r.nextDouble(), 0.2F);
                            world.setBlockState(check, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                        } else if (state.getBlock() instanceof FluidDrainable fluidDrainable) {
                            fluidDrainable.tryDrainFluid(null, world, check, state);
                        }
                    }
                }
            }
            world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 0.7F);
            world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }

    @Override
    public void tick() {
        // Activate as soon as it touches fluid
        super.tick();
        if (isInFluid()){onCollision(new HitResult(this.getPos()) {
            @Override
            public Type getType() {
                return Type.BLOCK;
            }
        });}
    }

    protected boolean isFluid(BlockState target){
        return !target.getFluidState().isEmpty();
    }
}
