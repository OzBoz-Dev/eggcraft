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
        this.setVelocity(0, 0.1, 0, 0.5f, 0.1f);
        World world = this.getWorld();
        if (!world.isClient()) {
            BlockPos center = this.getBlockPos();
            int r = 10;
            for (int x = -r; x <= r; x++) {
                for (int y = -r; y <= r; y++) {
                    for (int z = -r; z <= r; z++) {
                            BlockPos check = new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z);
                            BlockState state = world.getBlockState(check);
                            if (state.hasBlockEntity()) {
                                scannedBlockEntities.add(check);
                            }
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
            if (elapsedTicks % 52 == 0 && elapsedTicks < 180) this.getWorld().playSound(this, this.getBlockPos(), RegisterAll.OVERCLOCK_EGG_TICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            if (elapsedTicks == 5) this.getWorld().playSound(this, this.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 0.7F, 1.0F);
            if (elapsedTicks < 10){
                super.tick();
            }
            else {
                World world = this.getWorld();
                if (!world.isClient()) {
                    this.setNoGravity(true);
                    this.setVelocity(0, 0, 0);
                    Random r = Random.create();
                    if (elapsedTicks % 4 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.GLOW, this.getX()+(r.nextBetween(-1,1)*0.3f), this.getY(), this.getZ()+(r.nextBetween(-1,1)*0.3f), 1, 0, 0, 0, 0);
                    if (elapsedTicks == 170) this.getWorld().playSound(this, this.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (elapsedTicks > 200) {
                        active = false;
                        elapsedTicks = 0;
                        ((ServerWorld)world).spawnParticles(ParticleTypes.SONIC_BOOM, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                        this.discard();
                        return;
                    } else {
                        tickAppliances();
                        tickRandomTickingBlocks();
                    }
                }
            }
            elapsedTicks++;
        }
    }

    private void tickRandomTickingBlocks() {
        if (elapsedTicks % 2 == 0) {
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
            BlockState state = world.getBlockState(pos);
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null) {
                BlockEntityTicker<?> rawTicker = state.getBlockEntityTicker(world, entity.getType());
                if (rawTicker != null) {
                    @SuppressWarnings("unchecked")
                    BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>) rawTicker;
                    for (int i = 0; i < 4; i++) {
                        ticker.tick(world, pos, state, entity);
                    }
                    if (elapsedTicks%8 == 0) ((ServerWorld)world).spawnParticles(ParticleTypes.GLOW, pos.getX()+0.5f, pos.getY()+1, pos.getZ()+0.5f, 1,0,0,0,0);
                }
            }

        }
    }
}
