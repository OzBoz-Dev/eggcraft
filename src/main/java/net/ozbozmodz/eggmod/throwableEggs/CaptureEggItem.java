package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public class CaptureEggItem extends CustomEggItem{
    public CaptureEggItem(Item.Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public void spawnSequence(World world, PlayerEntity user, Hand hand, ItemStack itemStack) {
        ourEgg.setItem(itemStack);
        ourEgg.setPos(user.getX(), user.getEyeY(), user.getZ());
        ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
        Entity captive = getStoredEntity(itemStack, world);
        ((CaptureEggEntity)ourEgg).setCaptured(captive);
        world.spawnEntity(ourEgg);
    }

    public static Entity getStoredEntity(ItemStack stack, World world){
        NbtComponent dataComp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (dataComp == null) return null;
        NbtCompound entityData = dataComp.copyNbt();
        if (entityData == null || !entityData.contains("id")) return null;
        entityData.remove("UUID");
        return EntityType.loadEntityWithPassengers(entityData, world, entity -> entity);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent entityData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (entityData != null){
            NbtElement id = entityData.copyNbt().get("id");
            NbtElement hp = entityData.copyNbt().get("Health");
            if (id != null && hp != null)
                tooltip.add(
                    Text.translatable(id.asString()).append(
                    Text.literal(" | HP: " + hp.asString().replace('f',' '))).formatted(Formatting.GOLD)
                    );
            }
            super.appendTooltip(stack, context, tooltip, type);
    }
}
