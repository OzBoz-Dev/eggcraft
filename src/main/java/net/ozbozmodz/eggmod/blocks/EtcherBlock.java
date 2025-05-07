package net.ozbozmodz.eggmod.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.entities.EtcherBlockEntity;
import net.ozbozmodz.eggmod.util.RegisterItems;
import org.jetbrains.annotations.Nullable;

public class EtcherBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<EtcherBlock> CODEC = EtcherBlock.createCodec(EtcherBlock::new);

    public EtcherBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()){
            // If we can open the screen, then do so
            NamedScreenHandlerFactory screenHandlerFactory = ((EtcherBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null){
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) return null;
        return validateTicker(type, RegisterItems.ETCHER_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        // Our Block is being changed/broken
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EtcherBlockEntity){
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos, this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EtcherBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }
}
