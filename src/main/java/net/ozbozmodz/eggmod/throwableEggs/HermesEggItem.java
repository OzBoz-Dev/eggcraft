package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.EggHelper;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class HermesEggItem extends CustomEggItem{
    public HermesEggItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        EggHelper.setCooldowns(user, getTypeString());
        type = "hermes_egg";
        ourEgg = EggHelper.getType(type, world);
        ItemStack itemStack = user.getStackInHand(hand);
        if (ourEgg == null) return TypedActionResult.fail(itemStack);
        ourEgg.setOwner(user);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            ourEgg.setItem(itemStack);
            ourEgg.setPos(user.getX(), user.getEyeY(), user.getZ());
            ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 3.0f, 1.0f);
            world.spawnEntity(ourEgg);
            if (ourEgg.getOwner() != null) user.startRiding(ourEgg);
            }
        world.playSoundFromEntity(user, RegisterAll.HERMES_TAKEOFF, SoundCategory.PLAYERS, 1.0f, 1.0f);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, true);
    }
}
