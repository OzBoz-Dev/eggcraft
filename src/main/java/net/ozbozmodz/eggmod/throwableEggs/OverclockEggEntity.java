package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.ArrayList;
import java.util.List;

public class OverclockEggEntity extends CustomEggEntity{
    private boolean active;
    private int elapsedTicks;
    List<BlockPos> scannedBlockEntities;
    List<BlockPos> scannedRandomTickingBlocks;

    public OverclockEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
        active = false;
        elapsedTicks = 0;
        scannedBlockEntities = new ArrayList<>();
        scannedRandomTickingBlocks = new ArrayList<>();
    }

    public OverclockEggEntity(World world, LivingEntity owner) {
        super(world, owner);
        active = false;
        elapsedTicks = 0;
        scannedBlockEntities = new ArrayList<>();
        scannedRandomTickingBlocks = new ArrayList<>();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        // Float up
        this.setVelocity(0, 0.1, 0, 0.5f, 0.1f);
        World world = this.getWorld();
        if (!world.isClient()) {
            BlockPos center = this.getBlockPos();
            int r = 10;
            // Scan all nearby blocks, add them to two separate lists
            for (int x = -r; x <= r; x++) {
                for (int y = -r; y <= r; y++) {
                    for (int z = -r; z <= r; z++) {
                            BlockPos check = new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z);
                            BlockState state = world.getBlockState(check);
                            // Block entities are added to one list
                            if (state.hasBlockEntity()) {
                                scannedBlockEntities.add(check);
                            }
                            // All other blocks could be random ticking, add them to other list
                            else scannedRandomTickingBlocks.add(check);
                    }
                }
            }
        }
        active = true;
    }

    @Override
    public void tick() {
        if (!active) {
            super.tick();
        }
        else {
            // Sound logic
            if (elapsedTicks % 40 == 0 && elapsedTicks <= 280 && elapsedTicks != 0) this.getWorld().playSound(this, this.getBlockPos(), RegisterAll.OVERCLOCK_EGG_TICK, SoundCategory.PLAYERS, 1.0F, 1.3F);
            if (elapsedTicks == 10) this.getWorld().playSound(this, this.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 0.7F, 1.0F);
            // Allow it to float up before freezing
            if (elapsedTicks < 10){
                super.tick();
            }
            else {
                World world = this.getWorld();
                if (!world.isClient()) {
                    this.setNoGravity(true);
                    this.setVelocity(0, 0, 0);
                    Random r = Random.create();
                    // Particle logic
                    if (elapsedTicks % 4 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.GLOW, this.getX()+(r.nextBetween(-1,1)*0.3f), this.getY(), this.getZ()+(r.nextBetween(-1,1)*0.3f), 1, 0, 0, 0, 0);
                    if (elapsedTicks == 280) this.getWorld().playSound(this, this.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    // Discard the egg after a time, with sound/particles
                    if (elapsedTicks > 310) {
                        active = false;
                        elapsedTicks = 0;
                        ((ServerWorld)world).spawnParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                        this.discard();
                        return;
                    } else {
                        // Egg logic
                        tickAppliances();
                        tickRandomTickingBlocks();
                    }
                }
            }
            elapsedTicks++;
        }
    }

    private void tickRandomTickingBlocks() {
        // Blocks with random ticks will tick every 3 ticks, double check here just in case new blocks were added
        if (elapsedTicks % 3 == 0) {
            World world = this.getWorld();
            for (BlockPos pos : scannedRandomTickingBlocks) {
                BlockState state = world.getBlockState(pos);
                if (state.hasRandomTicks()) {
                    state.randomTick((ServerWorld) world, pos, Random.create());
                }
            }
        }
    }


    private void tickAppliances(){
        World world = this.getWorld();
        for (BlockPos pos : scannedBlockEntities) {
            // Get this block entity
            BlockState state = world.getBlockState(pos);
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null) {
                // Get the rawTicker for the block entity
                BlockEntityTicker<?> rawTicker = state.getBlockEntityTicker(world, entity.getType());
                if (rawTicker != null) {
                    // Cast the rawTicker to a Block Entity Ticker if not null
                    @SuppressWarnings("unchecked")
                    BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>) rawTicker;
                    // Perform 4 extra ticks on the block entity
                    for (int i = 0; i < 4; i++) {
                        ticker.tick(world, pos, state, entity);
                    }
                    // Spawn particles above the accelerated blocks
                    if (elapsedTicks%8 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.GLOW, pos.getX()+0.5f, pos.getY()+1, pos.getZ()+0.5f, 1,0,0,0,0);
                }
            }

        }
    }
}
