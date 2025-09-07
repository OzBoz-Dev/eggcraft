package net.ozbozmodz.eggmod.items;

import com.google.common.base.Predicates;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.EggHelper;
import net.ozbozmodz.eggmod.util.RegisterAll;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EggsInABasketItem extends Item {

    public EggsInABasketItem(Settings settings) {
        super(settings.maxCount(1).maxDamage(240));
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack result = new ItemStack(RegisterAll.EGGS_IN_A_BASKET_ITEM, 1);
        NbtCompound compound = new NbtCompound();
        compound.putDouble("effect", 1.0);
        compound.putInt("amplifier", 0);
        compound.putInt("ticks", 0);
        result.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound));
        return result;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Amplify the effect for 20 seconds, activate both items if you have another eggs basket in offhand
        setAmplifier(user.getStackInHand(hand), 1);
        setElapsedTicks(user.getStackInHand(hand), 0);
        if (hand == Hand.MAIN_HAND && user.getStackInHand(Hand.OFF_HAND).isOf(this)) use(world, user, Hand.OFF_HAND);
        user.getItemCooldownManager().set(this, 400);
        user.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 0.5f, 1.5f);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // If the amplified time is over, deactivate it
        setElapsedTicks(stack, getElapsedTicks(stack)+1);
        if (getElapsedTicks(stack) >= 400){
            if (getAmplifier(stack) == 1) entity.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE, 0.5f, 1.5f);
            setElapsedTicks(stack, 0);
            setAmplifier(stack, 0);
        }
        // Damage and change the stack only if it's equipped
        if (entity instanceof LivingEntity le) {
            if (le.getEquippedStack(EquipmentSlot.MAINHAND).isOf(this))
                applyStack(le.getEquippedStack(EquipmentSlot.MAINHAND), EquipmentSlot.MAINHAND, le);
            if (le.getEquippedStack(EquipmentSlot.OFFHAND).isOf(this))
                applyStack(le.getEquippedStack(EquipmentSlot.OFFHAND), EquipmentSlot.OFFHAND, le);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    /// Damage the stack depending on amplification state, add the effect
    public void applyStack(ItemStack stack, EquipmentSlot slot, LivingEntity le) {
        try {
            RegistryEntry<StatusEffect> effect = readEffect(stack);
            if (effect != null && effect.value() != null && getElapsedTicks(stack) % 20 == 0) {
                if (le.hasStatusEffect(effect)) return;
                stack.damage(getAmplifier(stack)+1, le, slot);
                StatusEffectInstance instance = new StatusEffectInstance(effect, 20, getAmplifier(stack), true, true, true);
                le.addStatusEffect(instance);
            }
        } catch (Exception e) {LoggerFactory.getLogger("eggmod").debug("No valid status effect on this basket");}
    }

    // Enchantable with Mending and Unbreaking
    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return enchantment.matches(Predicates.equalTo(Enchantments.UNBREAKING)) || enchantment.matches(Predicates.equalTo(Enchantments.MENDING));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getAmplifier(stack) != 0 || super.hasGlint(stack);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    /// Effect is saved as a double in NBT from the time of crafting
    public RegistryEntry<StatusEffect> readEffect(ItemStack stack){
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null) {
            NbtCompound data = component.copyNbt();
            double d = data.getDouble("effect");
            return switch ((int) d){
                case 1 -> StatusEffects.SPEED;
                case 2 -> StatusEffects.HASTE;
                case 3 -> StatusEffects.STRENGTH;
                case 4 -> StatusEffects.JUMP_BOOST;
                case 5 -> StatusEffects.REGENERATION;
                case 6 -> StatusEffects.DOLPHINS_GRACE;
                case 7 -> RegisterAll.REACH_EFFECT;
                default -> null;
            };
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        try {
            StatusEffect effect = readEffect(stack).value();
            if (effect != null) {
                String s = effect.getTranslationKey();
                tooltip.add(Text.literal("Effect: ").append(Text.translatable(s).formatted(Formatting.AQUA).formatted(Formatting.ITALIC)));
            }
        }
        catch (Exception ignored) {}
        EggHelper.appendTooltip(stack, context, tooltip, type, "item.eggmod.eggs_in_a_basket.tooltip");
        super.appendTooltip(stack, context, tooltip, type);
    }

    //NBT Shenanigans

    public void setAmplifier(ItemStack stack, int value){
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null){
            NbtCompound compound = component.copyNbt();
            compound.putInt("amplified", value);
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound));
        }
    }

    public void setElapsedTicks(ItemStack stack, int value){
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null){
            NbtCompound compound = component.copyNbt();
            compound.putInt("ticks", value);
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound));
        }
    }

    public int getAmplifier(ItemStack stack){
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null){
            NbtCompound compound = component.copyNbt();
            return compound.getInt("amplified");
        }
        return 0;
    }

    public int getElapsedTicks(ItemStack stack){
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null){
            NbtCompound compound = component.copyNbt();
            return compound.getInt("ticks");
        }
        return 0;
    }

}
