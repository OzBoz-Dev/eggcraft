package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ExcavatorEggEntity extends CustomEggEntity {
    public ExcavatorEggEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();
        if (!world.isClient()) {
            Direction dir = blockHitResult.getSide();
            BlockPos pos = blockHitResult.getBlockPos();
            boolean goX; boolean goY; boolean goZ;
            int fromX = -1; int fromY = -1; int fromZ = -1;
            int toX = -1; int toY = -1; int toZ = -1;
            // Decide which axis to mine along
            goY = dir == Direction.UP || dir == Direction.DOWN;
            goX = dir == Direction.EAST || dir == Direction.WEST;
            goZ = dir == Direction.NORTH || dir == Direction.SOUTH;

            if (goY) {
                fromX = -2; toX = 2; fromZ = -2; toZ = 2;
                // Down Direction = bottom of a block, meaning we mine up
                if (dir == Direction.DOWN) {
                    fromY = 0; toY = 4;
                }
                else {
                    fromY = -4; toY = 0;
                }
            }
            else if (goX) {
                // West Direction = west face of a block, meaning we mine east
                fromY = -2; toY = 2; fromZ = -2; toZ = 2;
                if (dir == Direction.WEST) {
                    fromX = 0; toX = 4;
                }
                else {
                    fromX = -4; toX = 0;
                }
            }
            else if (goZ) {
                // North direction = north side of a block, meaning we mine south
                fromY = -2; toY = 2; fromX = -2; toX = 2;
                if (dir == Direction.NORTH) {
                    fromZ = 0; toZ = 4;
                }
                else {
                    fromZ = -4; toZ = 0;
                }
            }
            ((ServerWorld)world).spawnParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(), 5, dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ(), 1);
            // Mine out a 5x5 using the parameters from before
            for (int i = fromX; i <= toX; i++) {
                for (int j = fromY; j <= toY; j++) {
                    for (int k = fromZ; k <= toZ; k++) {
                        BlockPos toBreak = new BlockPos(pos.getX()+i, pos.getY()+j, pos.getZ()+k);
                        // Only wooden pick blocks or blocks that need no items that aren't unbreakable
                        boolean canBreak = !world.getBlockState(toBreak).isToolRequired();
                        canBreak = canBreak || Items.WOODEN_PICKAXE.isCorrectForDrops(new ItemStack(Items.WOODEN_PICKAXE), world.getBlockState(toBreak));
                        // Attribute to player when possible
                        if (canBreak && world.getBlockState(toBreak).getHardness(world, toBreak) >= 0) {
                            if (this.getOwner() != null && this.getOwner() instanceof PlayerEntity){
                                world.breakBlock(toBreak, true, this.getOwner());
                            }
                            else world.breakBlock(toBreak, true);
                        }
                    }
                }
            }
            // Clean up
            world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }
}
