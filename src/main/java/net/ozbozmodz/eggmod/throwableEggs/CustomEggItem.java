package net.ozbozmodz.eggmod.throwableEggs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterItems;

/* Item which summons our egg entities */
public class CustomEggItem extends EggItem{
	public String type;
	protected CustomEggEntity ourEgg;

	public CustomEggItem(Item.Settings settings) {
		super(settings);
        type = Registries.ITEM.getId(this).getPath();
	}

    public String getTypeString(){
        return this.type;
    }

    /* Spawn the correct egg entity and set its default velocity */
	@Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        setCooldowns(user);
        type = Registries.ITEM.getId(this).getPath();
        ourEgg = getType(type, world, user);
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

    public static CustomEggEntity getType(String type, World world, PlayerEntity user){
        // Decide which egg must be summoned depending on the type string
        return switch (type) {
            case "blast_egg" -> new BlastEggEntity(world, user);
            case "iron_egg" -> new IronEggEntity(world, user);
            case "diamond_egg" -> new DiamondEggEntity(world, user);
            case "excavator_egg" -> new ExcavatorEggEntity(world, user);
            case "sponge_egg" -> new SpongeEggEntity(world, user);
            case "overclock_egg" -> new OverclockEggEntity(world, user);
            case "plaster_egg" -> new PlasterEggEntity(world, user);
            case "lure_egg" -> new LureEggEntity(world, user);
            default -> null;
        };
    }

    /* For being summoned by command or dispenser */
    public static CustomEggEntity getTypeNoUser(String type, World world){
        return switch (type) {
            case "blast_egg" -> new BlastEggEntity(RegisterItems.BLAST_EGG_ENTITY_ENTITY_TYPE, world);
            case "iron_egg" -> new IronEggEntity(RegisterItems.IRON_EGG_ENTITY_TYPE, world);
            case "diamond_egg" -> new DiamondEggEntity(RegisterItems.DIAMOND_EGG_ENTITY_TYPE, world);
            case "excavator_egg" -> new ExcavatorEggEntity(RegisterItems.EXCAVATOR_EGG_ENTITY_TYPE, world);
            case "sponge_egg" -> new SpongeEggEntity(RegisterItems.SPONGE_EGG_ENTITY_TYPE, world);
            case "overclock_egg" -> new OverclockEggEntity(RegisterItems.OVERCLOCK_EGG_ENTITY_TYPE, world);
            case "plaster_egg" -> new PlasterEggEntity(RegisterItems.PLASTER_EGG_ENTITY_TYPE, world);
            case "lure_egg" -> new LureEggEntity(RegisterItems.LURE_EGG_ENTITY_TYPE, world);
            default -> null;
        };
    }

    /* Set cooldowns for each egg type */
    public static void setCooldowns(PlayerEntity user){
        user.getItemCooldownManager().set(RegisterItems.BLAST_EGG_ITEM, 16);
        user.getItemCooldownManager().set(RegisterItems.IRON_EGG_ITEM, 8);
        user.getItemCooldownManager().set(RegisterItems.DIAMOND_EGG_ITEM, 8);
        user.getItemCooldownManager().set(RegisterItems.EXCAVATOR_EGG_ITEM, 16);
    }

}
