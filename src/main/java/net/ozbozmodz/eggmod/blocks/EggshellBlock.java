package net.ozbozmodz.eggmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.ozbozmodz.eggmod.util.RegisterItems;

public class EggshellBlock extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected static final IntProperty SHELLS = IntProperty.of("shells", 1, 9);
    public static final int MAX_SHELLS = 9;

    public EggshellBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(SHELLS,1));
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(RegisterItems.EGGSHELL_ITEM) && player.getAbilities().allowModifyWorld) {
            int i = state.get(SHELLS);
            world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
            world.playSound(player, pos, SoundEvents.BLOCK_TUFF_PLACE, SoundCategory.PLAYERS);
            // Add a shell to the pile
            if (i < MAX_SHELLS) {
                world.setBlockState(pos, state.with(SHELLS, i + 1), Block.NOTIFY_ALL);
                if (!player.isCreative()){
                    stack.decrement(1);
                }
                return ItemActionResult.SUCCESS;
            }
        }
        return  ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity){
        int numLayers = state.get(SHELLS);
        PlayerEntity nearestPlayer = world.getClosestPlayer(entity, 20);
        // Damage the entity that steps on it based on the number of EggshellBlock in the pile
        if (entity.isAttackable() &&
            entity.damage(entity.isPlayer() || nearestPlayer == null ? world.getDamageSources().generic() : world.getDamageSources().playerAttack(nearestPlayer), 0.8f * numLayers)){
            if (numLayers > 1)
                world.setBlockState(pos, state.with(SHELLS, numLayers - 1));
            else
                world.removeBlock(pos, false);
        }
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHELLS);
    }
}
