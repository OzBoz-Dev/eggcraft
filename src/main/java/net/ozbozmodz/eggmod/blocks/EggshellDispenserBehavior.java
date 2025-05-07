package net.ozbozmodz.eggmod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import net.ozbozmodz.eggmod.util.RegisterItems;

public class EggshellDispenserBehavior extends FallibleItemDispenserBehavior {

    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        // Which block is the dispenser facing?
        BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
        ServerWorld world = pointer.world();
        BlockState state = world.getBlockState(pos);
        BlockState eggState = RegisterItems.EGGSHELL_BLOCK.getDefaultState();
        // Get number of eggshell layers if the block we're facing is an eggshell block

        if (state.isOf(RegisterItems.EGGSHELL_BLOCK)){
            int numShells = state.get(EggshellBlock.SHELLS);
            // Add a layer to it
            if (numShells < EggshellBlock.MAX_SHELLS){
                world.setBlockState(pos, state.with(EggshellBlock.SHELLS, numShells + 1));
                this.setSuccess(true);
                stack.decrement(1);
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                return stack;
            }
            // If the eggshell is full, dispense failed
            else {
                this.setSuccess(false);
                return stack;
            }
        }
        // If we can place EggshellBlock here, then do so
        else if (world.getBlockState(pos).isAir() && eggState.canPlaceAt(world, pos)){
            world.setBlockState(pos, eggState.with(EggshellBlock.SHELLS, 1));
            world.emitGameEvent(null, GameEvent.BLOCK_PLACE, pos);
            this.setSuccess(true);
            stack.decrement(1);
            return stack;
        }
        // If we aren't facing an eggshell block AND cannot add one on adjacent block, just dispense
        else {
            return super.dispenseSilently(pointer, stack);
        }
    }
}
