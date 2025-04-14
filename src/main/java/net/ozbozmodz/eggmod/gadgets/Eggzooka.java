//package net.ozbozmodz.eggmod.gadgets;
//
//import java.util.List;
//import java.util.function.Predicate;
//
//import javax.imageio.stream.ImageInputStream;
//
//import org.jetbrains.annotations.ApiStatus.OverrideOnly;
//
//import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.item.TooltipContext;
//import net.minecraft.client.option.KeyBinding;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.projectile.thrown.EggEntity;
//import net.minecraft.inventory.Inventories;
//import net.minecraft.inventory.StackReference;
//import net.minecraft.item.EggItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.item.RangedWeaponItem;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.nbt.NbtElement;
//import net.minecraft.screen.PropertyDelegate;
//import net.minecraft.screen.slot.Slot;
//import net.minecraft.sound.SoundCategory;
//import net.minecraft.sound.SoundEvents;
//import net.minecraft.stat.Stats;
//import net.minecraft.tag.ItemTags;
//import net.minecraft.tag.TagKey;
//import net.minecraft.text.MutableText;
//import net.minecraft.text.Text;
//import net.minecraft.util.ClickType;
//import net.minecraft.util.Formatting;
//import net.minecraft.util.Hand;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.TypedActionResult;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.World;
//import net.ozbozmodz.eggmod.throwableEggs.blastEggEntity;
//import net.ozbozmodz.eggmod.throwableEggs.damageEgg;
//import net.ozbozmodz.eggmod.util.RegisterItems;
//import net.ozbozmodz.eggmod.util.modItemTags;
//
//public class Eggzooka extends RangedWeaponItem implements ImplementedInventory{
//    private static final String ITEMS_KEY = "Items";
//    public static final Predicate<ItemStack> EGGZOOKA_PROJECTILES = stack -> stack.isIn(modItemTags.EGGZOOKA_PROJECTILES);
//    private final DefaultedList<ItemStack> ammunition = DefaultedList.ofSize(2, ItemStack.EMPTY);
//    private int selectedSlot;
//
//    public Eggzooka(Settings settings) {
//        super(settings);
//        selectedSlot = 0;
//        for (int i = 0; i < 2; i++){
//            this.getStack(i).setCount(0);
//        }
//        //TODO Auto-generated constructor stub
//    }
//
//    @Override
//    public Predicate<ItemStack> getProjectiles() {
//        return EGGZOOKA_PROJECTILES;
//    }
//
//    @Override
//    public int getRange() {
//        return 30;
//    }
//
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        if (!world.isClient){
//            if (Screen.hasShiftDown()){
//                System.out.println("Switched from slot " + selectedSlot + " with " + this.getStack(selectedSlot).getName().getString());
//                selectedSlot++;
//                selectedSlot %= 2;
//                System.out.println("Switched to slot " + selectedSlot + " with " + this.getStack(selectedSlot).getName().getString());
//                user.getItemCooldownManager().set(this, 10);
//                return TypedActionResult.pass(user.getStackInHand(hand));
//            }
//            else if (!this.getStack(selectedSlot).isEmpty()){
//                this.spawnRelevant(world, user, this.getStack(selectedSlot));
//                return TypedActionResult.success(user.getStackInHand(hand));
//
//            }
//        }
//        return TypedActionResult.fail(user.getStackInHand(hand));
//    }
//
//
//    public void spawnRelevant(World world, PlayerEntity user, ItemStack itemStack){
//        Item item = itemStack.getItem();
//        EggEntity ourEgg = new EggEntity(world, user);
//        if (item instanceof EggItem) {
//            ourEgg = new EggEntity(world, user);
//        }
//        if (item instanceof damageEgg) {
//            if (((damageEgg)item).type.equals("BLASTEGG")){
//                ourEgg = new blastEggEntity(world, user);
//            }
//        }
//        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
//        if (!world.isClient) {
//            ourEgg.setItem(itemStack);
//            ourEgg.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2.0f, 1.0f);
//            world.spawnEntity(ourEgg);
//        }
//        user.incrementStat(Stats.USED.getOrCreateStat(this));
//        if (!user.getAbilities().creativeMode) {
//            itemStack.decrement(1);
//        }
//    }
//
//    @Override
//    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player,
//            StackReference cursorStackReference) {
//        if (player.getWorld().isClient()) return false;
//
//        if (clickType != ClickType.RIGHT || !slot.canTakePartial(player)) {
//            return false;
//        }
//        if (otherStack.isEmpty() && !this.isEmpty()){
//            cursorStackReference.set(this.getStack(selectedSlot).copy());
//            this.setStack(selectedSlot, otherStack.copy());
//            //setNbtData(stack, otherStack, player.getWorld());
//            return true;
//        }
//        else if (this.getStack(selectedSlot).isEmpty() && otherStack.isIn(modItemTags.EGGZOOKA_PROJECTILES)) {
//            System.out.println("Setting stack");
//            this.setStack(selectedSlot, otherStack.copy());
//            //setNbtData(stack, otherStack, player.getWorld());
//            otherStack.decrement(otherStack.getCount());
//            return true;
//        }
//        return false;
//    }
//
//    // public void setNbtData(ItemStack stack, ItemStack otherStack, World world){
//    //     if (!world.isClient()){
//    //         NbtCompound nbtData = stack.getOrCreateNbt();
//    //         Inventories.readNbt(nbtData, ammunition);
//    //         for (int i = 0; i < 2; i++){
//    //             if (i == selectedSlot){
//    //                 nbtData.putString("eggmod.eggzooka_slot_"+i, otherStack.getName().getString() + ":" + otherStack.getCount());
//    //                 System.out.println("CHANGEDSlot " + i + ": " + nbtData.getString("eggmod.eggzooka_slot_"+i));
//    //             }
//    //             else  {
//    //                 nbtData.putString("eggmod.eggzooka_slot_"+i, this.getStack(i).getName().getString() + ":" + this.getStack(0).getCount());
//    //                 System.out.println("Slot " + i + ": " + nbtData.getString("eggmod.eggzooka_slot_"+i));
//    //             }
//    //         }
//    //         Inventories.writeNbt(nbtData, ammunition);
//    //         markDirty();
//    //     }
//    //     // else {
//    //     //     NbtCompound nbtData = stack.getOrCreateNbt();
//    //     //     for (int i = 0; i < 2; i++){
//    //     //         String ammo = nbtData.getString("eggmod.eggzooka_slot_"+i);
//    //     //         System.out.println("Slot " + i + ": " + ammo);
//    //     //         String[] stuff = ammo.split(":");
//    //     //         // ItemStack set = new ItemStack(Items.AIR, 0);
//    //     //         // if (stuff[0].equals("Egg")) set = new ItemStack(Items.EGG, Integer.parseInt(stuff[1]));
//    //     //         // else if (stuff[0].equals("Blast Egg")) set = new ItemStack(Items.EGG, Integer.parseInt(stuff[1]));
//    //     //         // this.setStack(i, set);
//    //     //     }
//    //     // }
//    // }
//
//    @Override
//    public DefaultedList<ItemStack> getItems() {
//        return ammunition;
//    }
//
//    // @Override
//    // public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
//    //     if (Screen.hasShiftDown()){
//    //         tooltip.add(Text.translatable("item.eggmod.eggzooka.tooltip_2"));
//    //         for (int i = 0; i < 2; i++){
//    //             Text t = this.getStack(i).getName();
//    //             if (t.asTruncatedString(100).equals("Air"))
//    //                 t = Text.of("Empty");
//    //             tooltip.add(Text.translatable("item.eggmod.eggzooka.tooltip_sub").append(Text.of(""+i + ": ")).append(((MutableText)t).formatted(Formatting.AQUA)));
//    //         }
//    //         return;
//    //     }
//    //     Text t = this.getStack(selectedSlot).getName();
//    //             if (t.asTruncatedString(100).equals("Air"))
//    //                 t = Text.of("Empty");
//    //     tooltip.add(Text.translatable("item.eggmod.eggzooka.tooltip_1").append(((MutableText)t).formatted(Formatting.AQUA)));
//    // }
//
//
//
//}
