package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class HermesEggItem extends CustomEggItem{
    public HermesEggItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void spawnSequence(World world, PlayerEntity user, Hand hand, ItemStack itemStack) {
        ourEgg.setItem(itemStack);
        ourEgg.setPos(user.getX(), user.getEyeY(), user.getZ());
        ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2.5f, 1.0f);
        world.spawnEntity(ourEgg);
        if (ourEgg.getOwner() != null) user.startRiding(ourEgg);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSoundFromEntity(user, RegisterAll.HERMES_TAKEOFF, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return super.use(world, user, hand);
    }
}
