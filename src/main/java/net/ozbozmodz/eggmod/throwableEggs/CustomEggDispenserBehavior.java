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
        CustomEggItem ourEggItem = (CustomEggItem) stack.getItem();
        String typeString = ourEggItem.getTypeString();
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        BlockPos pos = pointer.pos();
        CustomEggEntity ourEgg = EggHelper.getType(typeString, world);
        if (ourEgg != null) {
            // If we have an experience egg
            if (ourEggItem instanceof ExperienceEggItem) ((ExperienceEggEntity)ourEgg).setExperience(ExperienceEggItem.getExperience(stack));
            if (ourEggItem instanceof CaptureEggItem) ((CaptureEggEntity)ourEgg).setCaptured(CaptureEggItem.getStoredEntity(stack, world));
            ourEgg.setItem(stack);
            ourEgg.setPos(pos.getX() + direction.getOffsetX(), pos.getY() + direction.getOffsetY()+0.5, pos.getZ() + direction.getOffsetZ());
            ourEgg.setVelocity(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
            world.spawnEntity(ourEgg);
            stack.decrement(1);
        }
        return stack;
    }
}
