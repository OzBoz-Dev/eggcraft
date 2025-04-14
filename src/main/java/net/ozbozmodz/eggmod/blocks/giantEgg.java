package net.ozbozmodz.eggmod.blocks;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class giantEgg extends Block {

    public static final int MAX_BITES = 6;
    public static final IntProperty BITES;


    public giantEgg(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(BITES, 0));
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("item.eggmod.giant_egg.tooltip").formatted(Formatting.ITALIC));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return modVoxelShapes.GIANT_EGG_SHAPES[state.get(BITES)];
    }

    // When the player uses the block
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){
        if (world.isClient()){
            return tryEat(world, pos, state, player);
        }
        return tryEat(world, pos, state, player);
    }

    protected static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false)){
            return ActionResult.PASS;
        }
        else {
            // Fill Hunger and Saturation + Emit a game event
            player.getHungerManager().add(2, 0.4F);
            int i = (Integer) state.get(BITES);
            world.emitGameEvent(player, GameEvent.EAT, pos);
            world.playSound(player, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS);
            // Add a bite
            if (i < 6) {
                world.setBlockState(pos, state.with(BITES, i + 1), Block.NOTIFY_ALL);
            }
            // If at max bites, destroy the block
            else {
                world.playSound(player, pos, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS);
                world.removeBlock(pos, false);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }
            // Return Success
            return ActionResult.SUCCESS;
        }
    }

    // Append BITES property
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BITES});
    }

    // Just like cake, has comparator output
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    // Same logic on comparators as cake
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return (7 - (Integer) state.get(BITES)) * 2;
    }

    static {
        BITES = Properties.BITES;
    }



}
