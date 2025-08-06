package net.ozbozmodz.eggmod.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.entities.ExperienceCatcherEntity;
import net.ozbozmodz.eggmod.util.RegisterAll;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ExperienceCatcherBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final IntProperty EXPERIENCE = IntProperty.of("experience", 0, 10);
    public static final MapCodec<ExperienceCatcherBlock> CODEC = ExperienceCatcherBlock.createCodec(ExperienceCatcherBlock::new);

    // Set experience level and direction
    public ExperienceCatcherBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(EXPERIENCE, 0));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()){
            // If we can open the screen, then do so
            NamedScreenHandlerFactory screenHandlerFactory = ((ExperienceCatcherEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null){
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) return null;
        return validateTicker(type, RegisterAll.EXPERIENCE_CATCHER_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        // Our Block is being changed/broken
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ExperienceCatcherEntity){
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                if (world.getBlockEntity(pos) != null){
                    int experience = ((ExperienceCatcherEntity) Objects.requireNonNull(world.getBlockEntity(pos))).experience;
                    while (experience >= 50){
                        world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), 50));
                        experience -= 50;
                    }
                    if (experience > 0) world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), experience));

                }
                world.updateComparators(pos, this);

            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    // Add properties energy state and direction
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(EXPERIENCE);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExperienceCatcherEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ModVoxelShapes.EXPERIENCE_CATCHER_SHAPE;
    }
}
