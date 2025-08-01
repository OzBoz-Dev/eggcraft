package net.ozbozmodz.eggmod.items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.throwableEggs.CustomEggEntity;
import net.ozbozmodz.eggmod.throwableEggs.CustomEggItem;
import net.ozbozmodz.eggmod.util.EggHelper;
import net.ozbozmodz.eggmod.util.RegisterAll;
import net.ozbozmodz.eggmod.util.ModItemTags;
import org.jetbrains.annotations.Nullable;

public class Eggzooka extends RangedWeaponItem {
    public static final Predicate<ItemStack> EGGZOOKA_PROJECTILES = stack -> stack.isIn(ModItemTags.EGGZOOKA_PROJECTILES);
    private int selectedSlot;

    public Eggzooka(Settings settings) {
        super(settings);
        selectedSlot = 0;
    }

    public static List<ItemStack> defaultList(){
        List<ItemStack> comp = new ArrayList<>(2);
        for (int i = 0; i < 2; i++){
            comp.add(ItemStack.EMPTY);
        }
        return comp;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return EGGZOOKA_PROJECTILES;
    }

    @Override
    public int getRange() {
        return 30;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {

    }

//    @Override
//    public ItemStack getDefaultStack() {
//        System.out.println("Generating Default Stack...");
//        ItemStack result = new ItemStack(this);
//        List<ItemStack> comp = defaultList();
//        result.set(RegisterAll.EGG_INV, comp);
//        return result;
//    }


    protected ItemStack getCurrentProjectile(int index, ItemStack heldWeapon){
        List<ItemStack> projectiles = heldWeapon.get(RegisterAll.EGG_INV);
        if (projectiles != null) return projectiles.get(index);
        return ItemStack.EMPTY;
    }

    protected void setCurrentProjectile(int index, ItemStack heldWeapon, ItemStack newProjectile){
        List<ItemStack> result = heldWeapon.get(RegisterAll.EGG_INV);
        if (result != null){
            System.out.println("Setting Projectile!");
            result.set(index, newProjectile);
            heldWeapon.set(RegisterAll.EGG_INV, result);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient){
            if (Screen.hasShiftDown()){
                user.sendMessage(Text.literal("Switched from slot " + selectedSlot + " with " + getCurrentProjectile(selectedSlot, user.getStackInHand(hand)).getName().getString()));
                selectedSlot++;
                selectedSlot %= 2;
                user.sendMessage(Text.literal("Switched to slot " + selectedSlot + " with " + getCurrentProjectile(selectedSlot, user.getStackInHand(hand)).getName().getString()));
                return TypedActionResult.pass(user.getStackInHand(hand));
            }
            else if (!getCurrentProjectile(selectedSlot, user.getStackInHand(hand)).isEmpty()){
                spawnRelevant(world, user, user.getStackInHand(hand), getCurrentProjectile(selectedSlot, user.getStackInHand(hand)));
                return TypedActionResult.success(user.getStackInHand(hand));

            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }


    public void spawnRelevant(World world, PlayerEntity user, ItemStack weaponStack, ItemStack projectile){
        Item item = projectile.getItem();
        CustomEggEntity ourEgg = null;
        if (item instanceof CustomEggItem) {
            ourEgg = EggHelper.getType(((CustomEggItem) item).type, world);
        }
        if (!world.isClient && ourEgg != null) {
            ourEgg.setOwner(user);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            ourEgg.setItem(projectile);
            ourEgg.setPos(user.getX(), user.getEyeY(), user.getZ());
            ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2.0f, 1.0f);
            world.spawnEntity(ourEgg);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            projectile.decrement(1);
            setCurrentProjectile(selectedSlot, weaponStack, projectile);
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player,
            StackReference cursorStackReference) {
        if (player.getAbilities().creativeMode){
            if (!player.getWorld().isClient) return false;
        }
        else if (player.getWorld().isClient()) return false;
        ItemStack projectile = getCurrentProjectile(selectedSlot, stack);

        if (clickType != ClickType.RIGHT || !slot.canTakePartial(player)) {
            return false;
        }
        if (otherStack.isEmpty() && !projectile.isEmpty()){
            cursorStackReference.set(projectile.copy());
            setCurrentProjectile(selectedSlot, stack, ItemStack.EMPTY);
            return true;
        }
        else if (getCurrentProjectile(selectedSlot, stack).isEmpty() && (otherStack.getItem() instanceof CustomEggItem)) {
            ItemStack copy = otherStack.copy();
            otherStack.decrement(otherStack.getCount());
            setCurrentProjectile(selectedSlot, stack, copy);
            return true;
        }
        return false;
    }

    // @Override
    // public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
    //     if (Screen.hasShiftDown()){
    //         tooltip.add(Text.translatable("item.eggmod.eggzooka.tooltip_2"));
    //         for (int i = 0; i < 2; i++){
    //             Text t = getStack(i).getName();
    //             if (t.asTruncatedString(100).equals("Air"))
    //                 t = Text.of("Empty");
    //             tooltip.add(Text.translatable("item.eggmod.eggzooka.tooltip_sub").append(Text.of(""+i + ": ")).append(((MutableText)t).formatted(Formatting.AQUA)));
    //         }
    //         return;
    //     }
    //     Text t = getStack(selectedSlot).getName();
    //             if (t.asTruncatedString(100).equals("Air"))
    //                 t = Text.of("Empty");
    //     tooltip.add(Text.translatable("item.eggmod.eggzooka.tooltip_1").append(((MutableText)t).formatted(Formatting.AQUA)));
    // }



}
