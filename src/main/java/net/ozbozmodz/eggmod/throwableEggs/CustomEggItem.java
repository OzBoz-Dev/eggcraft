package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.EggHelper;

import java.util.List;

/* Item which summons our egg entities */
public class CustomEggItem extends EggItem {
    public String type;
    protected CustomEggEntity ourEgg;

    public CustomEggItem(Item.Settings settings) {
        super(settings);
        type = Registries.ITEM.getId(this).getPath();
    }

    public String getTypeString() {
        return this.type;
    }

    /* Spawn the correct egg entity and set its default velocity */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        EggHelper.setCooldowns(user, getTypeString());
        type = Registries.ITEM.getId(this).getPath();
        ourEgg = EggHelper.getType(type, world, user);
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            ourEgg.setItem(itemStack);
            ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(ourEgg);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, true);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        // Grab the corresponding tooltip
        EggHelper.appendTooltip(stack, context, tooltip, type, "item.eggmod." + Registries.ITEM.getId(stack.getItem()).getPath() + ".tooltip");
    }
}

