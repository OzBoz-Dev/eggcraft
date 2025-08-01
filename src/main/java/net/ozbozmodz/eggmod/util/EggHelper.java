package net.ozbozmodz.eggmod.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.items.TemplateItem;
import net.ozbozmodz.eggmod.throwableEggs.*;

import java.util.List;

public class EggHelper {
    // Maintain associations for our custom eggs

    // Convert our template item to the corresponding output item
    public static CustomEggItem getCurrentOutputItem(Item template){
        return switch(((TemplateItem)template).getType()){
            case "blast_egg_template" -> (CustomEggItem) RegisterAll.BLAST_EGG_ITEM;
            case "iron_egg_template" -> (CustomEggItem) RegisterAll.IRON_EGG_ITEM;
            case "diamond_egg_template" -> (CustomEggItem) RegisterAll.DIAMOND_EGG_ITEM;
            case "excavator_egg_template" -> (CustomEggItem) RegisterAll.EXCAVATOR_EGG_ITEM;
            case "sponge_egg_template" -> (CustomEggItem) RegisterAll.SPONGE_EGG_ITEM;
            case "overclock_egg_template" -> (CustomEggItem) RegisterAll.OVERCLOCK_EGG_ITEM;
            case "plaster_egg_template" -> (CustomEggItem) RegisterAll.PLASTER_EGG_ITEM;
            case "lure_egg_template" -> (CustomEggItem) RegisterAll.LURE_EGG_ITEM;
            case "target_egg_template" -> (CustomEggItem) RegisterAll.TARGET_EGG_ITEM;
            case "hermes_egg_template" -> (CustomEggItem) RegisterAll.HERMES_EGG_ITEM;
            default -> null;
        };
    }

    /* For being summoned by command or dispenser */
    public static CustomEggEntity getType(String type, World world){
        return switch (type) {
            case "blast_egg" -> new BlastEggEntity(RegisterAll.BLAST_EGG_ENTITY_ENTITY_TYPE, world);
            case "iron_egg" -> new IronEggEntity(RegisterAll.IRON_EGG_ENTITY_TYPE, world);
            case "diamond_egg" -> new DiamondEggEntity(RegisterAll.DIAMOND_EGG_ENTITY_TYPE, world);
            case "excavator_egg" -> new ExcavatorEggEntity(RegisterAll.EXCAVATOR_EGG_ENTITY_TYPE, world);
            case "sponge_egg" -> new SpongeEggEntity(RegisterAll.SPONGE_EGG_ENTITY_TYPE, world);
            case "overclock_egg" -> new OverclockEggEntity(RegisterAll.OVERCLOCK_EGG_ENTITY_TYPE, world);
            case "plaster_egg" -> new PlasterEggEntity(RegisterAll.PLASTER_EGG_ENTITY_TYPE, world);
            case "lure_egg" -> new LureEggEntity(RegisterAll.LURE_EGG_ENTITY_TYPE, world);
            case "target_egg" -> new TargetEggEntity(RegisterAll.TARGET_EGG_ENTITY_TYPE, world);
            case "hermes_egg" -> new HermesEggEntity(RegisterAll.TARGET_EGG_ENTITY_TYPE, world);
            default -> null;
        };
    }

    /* Set cooldowns for each egg type */
    public static void setCooldowns(PlayerEntity user, String type){
        switch (type) {
            case "blast_egg" -> user.getItemCooldownManager().set(RegisterAll.BLAST_EGG_ITEM, 16);
            case "iron_egg" -> user.getItemCooldownManager().set(RegisterAll.IRON_EGG_ITEM, 8);
            case "diamond_egg" -> user.getItemCooldownManager().set(RegisterAll.DIAMOND_EGG_ITEM, 8);
            case "excavator_egg" -> user.getItemCooldownManager().set(RegisterAll.EXCAVATOR_EGG_ITEM, 16);
            case "sponge_egg" -> user.getItemCooldownManager().set(RegisterAll.SPONGE_EGG_ITEM, 16);
            case "overclock_egg" -> user.getItemCooldownManager().set(RegisterAll.OVERCLOCK_EGG_ITEM, 200);
            case "plaster_egg" -> user.getItemCooldownManager().set(RegisterAll.PLASTER_EGG_ITEM, 16);
            case "lure_egg" -> user.getItemCooldownManager().set(RegisterAll.LURE_EGG_ITEM, 200);
            case "target_egg" -> user.getItemCooldownManager().set(RegisterAll.TARGET_EGG_ITEM, 50);
            case "hermes_egg" -> user.getItemCooldownManager().set(RegisterAll.HERMES_EGG_ITEM, 10);
        }
    }

    // Quickly set tooltips, prompt to hold shift when shift isn't down, and show the corresponding tooltip when it is
    public static void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, String tipKey) {
        if (Screen.hasShiftDown()){
            tooltip.add(Text.translatable(tipKey).formatted(Formatting.ITALIC).formatted(Formatting.DARK_AQUA));
        }
        else {
            tooltip.add(Text.translatable("misc.eggmod.shiftprompt.tooltip").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
        }
    }


    }
