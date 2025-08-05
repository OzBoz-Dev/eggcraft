package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class ExperienceEggItem extends CustomEggItem{
    public static int maxExperience;

    public ExperienceEggItem(Item.Settings settings) {
        super(settings.maxCount(1));
        maxExperience = 500;
    }

    @Override
    public String getTypeString() {
        return "experience_egg";
    }

    @Override
    public void spawnSequence(World world, PlayerEntity user, Hand hand, ItemStack itemStack) {
        ourEgg.setItem(itemStack);
        ourEgg.setPos(user.getX(), user.getEyeY(), user.getZ());
        ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
        getExperience(itemStack);
        ((ExperienceEggEntity)ourEgg).setExperience(getExperience(itemStack));
        world.spawnEntity(ourEgg);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (getExperience(stack) == 0) return TypedActionResult.fail(stack);
        return super.use(world, user, hand);
    }

    public static void addExp(int addExperience, ItemStack stack){
        int experience = getExperience(stack);
        experience += addExperience;
        if (experience > maxExperience) experience = maxExperience;
        NbtCompound data = new NbtCompound();
        // Keeping NBT Components for backwards compatibility purposes
        data.putInt("experience", experience);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(data));
    }

    public static int getExperience(ItemStack stack){
        NbtComponent dataComp = stack.get(DataComponentTypes.CUSTOM_DATA);
        int experience = 0;
        if (dataComp != null && dataComp.copyNbt() != null){
            experience = dataComp.copyNbt().getInt("experience");
        }
        return experience;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        getExperience(stack);
        tooltip.add(Text.literal("Experience: " + getExperience(stack) + "/" + maxExperience).formatted(Formatting.GREEN));
    }




}
