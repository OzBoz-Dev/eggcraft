package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;



public class PlasterEggEntity extends CustomEggEntity {
    boolean firstTick;
    BlockPos origin;
    public PlasterEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
        firstTick = true;
        origin = new BlockPos(0,0,0);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();
        if (!world.isClient()) {
            if (this.getOwner() != null) origin = this.getOwner().getBlockPos();
            BlockPos center = new BlockPos((int) blockHitResult.getPos().getX(), (int) blockHitResult.getPos().getY(), (int) blockHitResult.getPos().getZ());
            int r = 4;
            // Replace air with dirt in an r block radius, but restrict height to only up to the player's feet
            for (int x = -r; x <= r; x++) {
                for (int y = 0; y <= Math.min(origin.getY() - (center.getY()+1), 15) ; y++) {
                    for (int z = -r; z <= r; z++) {
                        double d = Math.sqrt(Math.pow(x,2) + Math.pow(z,2));
                        if (d > (r)) continue;
                        BlockPos check = new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z);
                        BlockState state = world.getBlockState(check);
                        if (state.getBlock() == Blocks.AIR) {
                            world.setBlockState(check, Blocks.DIRT.getDefaultState(), Block.NOTIFY_ALL);
                            world.playSound(this, this.getBlockPos(), SoundEvents.BLOCK_ROOTED_DIRT_PLACE, SoundCategory.PLAYERS, 0.1F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        // Set origin in case it was spawned via commands or dispenser (to avoid crashes)
        if (firstTick){
            if (this.getOwner() != null) origin = this.getOwner().getBlockPos();
            else origin = this.getBlockPos();
            firstTick = false;
        }
    }
}
