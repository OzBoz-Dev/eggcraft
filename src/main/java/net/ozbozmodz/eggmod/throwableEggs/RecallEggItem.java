package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class RecallEggItem extends CustomEggItem{

    public RecallEggItem(Item.Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getRecallPos(stack) != null;
    }

    @Override
    public String getTypeString() {
        return "recall_egg";
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
            if (user.isSneaking()){
                String extra = "";
                ItemStack offStack = user.getOffHandStack();
                // Check offhand for a compass or recovery compass, bind those instead
                if (offStack.isOf(Items.COMPASS) && offStack.get(DataComponentTypes.LODESTONE_TRACKER) != null) {
                    LodestoneTrackerComponent ltc = offStack.get(DataComponentTypes.LODESTONE_TRACKER);
                    if (ltc != null && ltc.target().isPresent()){
                        bindPos(ltc.target().get().pos(), ltc.target().get().dimension(), stack);
                        extra = "lodestone at ";
                    }
                }
                else if (offStack.isOf(Items.RECOVERY_COMPASS) && user.getLastDeathPos().isPresent()){
                    bindPos(user.getLastDeathPos().get().pos(), user.getLastDeathPos().get().dimension(), stack);
                    extra = "death position at ";
                }
                // Else bind the user's current pos
                else {
                    bindPos(user.getBlockPos(), user.getWorld().getRegistryKey(), stack);
                    extra = "current position at ";
                }
                BlockPos pos = getRecallPos(stack).pos();
                user.sendMessage(Text.literal("Set to " + extra + pos.getX() + ", " + pos.getY() +", "+ pos.getZ()
                        + " in " + getRecallPos(stack).dimension().getValue()).formatted(Formatting.GREEN), true);
                world.playSound(user, user.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1,1);
                user.getItemCooldownManager().set(this, 10);
                return TypedActionResult.success(stack);
            }
            // If not holding shift
            else {
                // If no position, inform the user and fail
                if (getRecallPos(stack) == null) {
                    user.sendMessage(Text.literal("No recall position set").formatted(Formatting.RED), true);
                    return TypedActionResult.fail(stack);
                }
                // Otherwise, begin teleporting
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 140, 2));
                world.playSound(user, user.getBlockPos(), SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.PLAYERS, 1,1);
                user.getItemCooldownManager().set(this, 100);
                return ItemUsage.consumeHeldItem(world, user, hand);
            }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // Perform the teleport
        if (!world.isClient()) {
            ServerWorld nextWorld = Objects.requireNonNull(world.getServer()).getWorld(getRecallPos(stack).dimension());
            BlockPos nextPos = getRecallPos(stack).pos();
            if (user.canTeleportBetween(world, nextWorld)) {
                user.teleport(nextWorld, nextPos.getX(), nextPos.getY(), nextPos.getZ(), EnumSet.noneOf(PositionFlag.class), 0.0f, 0.0f);
            }
        }
        stack.decrementUnlessCreative(1, user);
        if (user instanceof PlayerEntity pe) pe.incrementStat(Stats.USED.getOrCreateStat(this));
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 80;
    }

    // Spawn some fancy particles
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        Vec3d particle = user.getPos().add(new Vec3d(Math.cos(remainingUseTicks*2),2,Math.sin(remainingUseTicks*2)).multiply((double) remainingUseTicks / 80));
        if (!world.isClient()) ((ServerWorld)world).spawnParticles(ParticleTypes.PORTAL, particle.x, particle.y, particle.z, 3, 0,0.3f,0,-1);
    }

    public static void bindPos(BlockPos pos, RegistryKey<World> worldKey, ItemStack stack){
        stack.set(RegisterAll.RECALL_POS, new GlobalPos(worldKey, pos));
    }

    public static GlobalPos getRecallPos(ItemStack stack){
        return stack.get(RegisterAll.RECALL_POS);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (getRecallPos(stack) != null) {
            BlockPos pos = getRecallPos(stack).pos();
            tooltip.add(Text.literal("Bound to: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " in " + getRecallPos(stack).dimension().getValue()));
        }
        if (Screen.hasShiftDown()){
            tooltip.add(Text.translatable("item.eggmod.recall_egg.tooltip_1").formatted(Formatting.ITALIC).formatted(Formatting.DARK_AQUA));
            tooltip.add(Text.translatable("item.eggmod.recall_egg.tooltip_2").formatted(Formatting.ITALIC).formatted(Formatting.DARK_AQUA));
        }
        else {
            tooltip.add(Text.translatable("misc.eggmod.shiftprompt.tooltip").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
        }
    }
}
