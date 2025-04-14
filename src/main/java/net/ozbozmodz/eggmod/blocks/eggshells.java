package net.ozbozmodz.eggmod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class eggshells extends SnowBlock{

    public eggshells(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity){
        int numLayers = state.get(LAYERS);
        if (entity.isAttackable() && 
            entity.damage(entity.isPlayer() ? world.getDamageSources().generic() : world.getDamageSources().playerAttack(world.getClosestPlayer(entity, 20)), 0.8f * numLayers)){
            if (numLayers > 1)
                world.setBlockState(pos, state.with(LAYERS, numLayers - 1));
            else
                world.removeBlock(pos, false);
        }
    }

    
}
