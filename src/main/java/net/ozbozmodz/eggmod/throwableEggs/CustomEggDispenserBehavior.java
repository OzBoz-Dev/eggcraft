package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.ozbozmodz.eggmod.util.EggHelper;

public class CustomEggDispenserBehavior extends ProjectileDispenserBehavior {
    public CustomEggDispenserBehavior(Item item) {
        super(item);
    }

    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        ServerWorld world = pointer.world();
        String typeString = ((CustomEggItem)stack.getItem()).getTypeString();
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        BlockPos pos = pointer.pos();
        CustomEggEntity ourEgg = EggHelper.getTypeNoUser(typeString, world);
        if (ourEgg != null) {
            ourEgg.setItem(stack);
            ourEgg.setPos(pos.getX() + direction.getOffsetX(), pos.getY() + direction.getOffsetY(), pos.getZ() + direction.getOffsetZ());
            ourEgg.setVelocity(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
            world.spawnEntity(ourEgg);
            stack.decrement(1);
        }
        return stack;
    }
}
