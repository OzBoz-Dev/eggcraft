package net.ozbozmodz.eggmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class ModVoxelShapes {
    public static final VoxelShape[] GIANT_EGG_SHAPES = new VoxelShape[]{
            VoxelShapes.union(Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(2.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(1.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(2.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(3.0, 11.0, 3.0, 13.0, 13.0, 13.0), Block.createCuboidShape(5.0, 13.0, 5.0, 11.0, 14.0, 11.0) , Block.createCuboidShape(5.0, 14.0, 5.0, 11.0, 15.0, 11.0) , Block.createCuboidShape(6.0, 15.0, 6.0, 10.0, 16.0, 10.0)
                    ).simplify(),
                    VoxelShapes.union(
                            Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(2.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(2.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(2.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(3.0, 11.0, 3.0, 13.0, 13.0, 13.0), Block.createCuboidShape(5.0, 13.0, 5.0, 11.0, 14.0, 11.0), Block.createCuboidShape(5.0, 14.0, 5.0, 11.0, 15.0, 11.0), Block.createCuboidShape(6.0, 15.0, 6.0, 10.0, 16.0, 10.0)
                    ).simplify(),
                    VoxelShapes.union(
                            Block.createCuboidShape(4.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(4.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(4.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(4.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(4.0, 11.0, 3.0, 13.0, 13.0, 13.0), Block.createCuboidShape(5.0, 13.0, 5.0, 11.0, 14.0, 11.0), Block.createCuboidShape(5.0, 14.0, 5.0, 11.0, 15.0, 11.0), Block.createCuboidShape(6.0, 15.0, 6.0, 10.0, 16.0, 10.0)
                    ).simplify(),
                    VoxelShapes.union(
                            Block.createCuboidShape(6.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(6.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(6.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(6.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(6.0, 11.0, 3.0, 13.0, 13.0, 13.0), Block.createCuboidShape(6.0, 13.0, 5.0, 11.0, 14.0, 11.0), Block.createCuboidShape(6.0, 14.0, 5.0, 11.0, 15.0, 11.0), Block.createCuboidShape(6.0, 15.0, 6.0, 10.0, 16.0, 10.0)
                    ).simplify(),
                    VoxelShapes.union(
                            Block.createCuboidShape(8.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(8.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(8.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(8.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(8.0, 11.0, 3.0, 13.0, 13.0, 13.0), Block.createCuboidShape(8.0, 13.0, 5.0, 11.0, 14.0, 11.0), Block.createCuboidShape(8.0, 14.0, 5.0, 11.0, 15.0, 11.0), Block.createCuboidShape(8.0, 15.0, 6.0, 10.0, 16.0, 10.0)
                    ).simplify(),
                    VoxelShapes.union(
                            Block.createCuboidShape(10.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(10.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(10.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(10.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(10.0, 11.0, 3.0, 13.0, 13.0, 13.0), Block.createCuboidShape(10.0, 13.0, 5.0, 11.0, 14.0, 11.0), Block.createCuboidShape(10.0, 14.0, 5.0, 11.0, 15.0, 11.0)
                    ).simplify(),
                    VoxelShapes.union(
                            Block.createCuboidShape(12.0, 0.0, 3.0, 13.0, 1.0, 13.0), Block.createCuboidShape(12.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.createCuboidShape(12.0, 3.0, 1.0, 15.0, 8.0, 15.0), Block.createCuboidShape(12.0, 8.0, 2.0, 14.0, 11.0, 14.0), Block.createCuboidShape(12.0, 11.0, 3.0, 13.0, 13.0, 13.0)
                    ).simplify()
    };
}
