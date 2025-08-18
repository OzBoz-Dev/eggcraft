package net.ozbozmodz.eggmod.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.items.TemplateItem;
import net.ozbozmodz.eggmod.throwableEggs.*;

import java.util.List;

public class EggHelper {
    // Maintain associations for our custom eggs

    // Convert our template item to the corresponding output item
    public static CustomEggItem getCurrentOutputItem(Item template){
        Item output = Registries.ITEM.get(Identifier.of ("eggmod", ((TemplateItem)template).getType()));
        if (output instanceof CustomEggItem customEggItem){
            return customEggItem;
        }
        else return null;
    }

    /* Return the right egg entity */
    public static CustomEggEntity getType(String type, World world){
        EntityType<?> entityType = Registries.ENTITY_TYPE.get(Identifier.of("eggmod", type.concat("_entity")));
        if (!(entityType.create(world) instanceof CustomEggEntity entity)) return null;
        return entity;
    }

    /* Set cooldowns for each egg type */
    public static void setCooldowns(PlayerEntity user, String type){
        switch (type) {
            case "blast_egg" -> user.getItemCooldownManager().set(RegisterAll.BLAST_EGG_ITEM, 16);
            case "iron_egg" -> user.getItemCooldownManager().set(RegisterAll.IRON_EGG_ITEM, 8);
            case "diamond_egg" -> user.getItemCooldownManager().set(RegisterAll.DIAMOND_EGG_ITEM, 8);
            case "excavator_egg" -> user.getItemCooldownManager().set(RegisterAll.EXCAVATOR_EGG_ITEM, 16);
            case "sponge_egg" -> user.getItemCooldownManager().set(RegisterAll.SPONGE_EGG_ITEM, 16);
            case "overclock_egg" -> user.getItemCooldownManager().set(RegisterAll.OVERCLOCK_EGG_ITEM, 20);
            case "plaster_egg" -> user.getItemCooldownManager().set(RegisterAll.PLASTER_EGG_ITEM, 16);
            case "lure_egg" -> user.getItemCooldownManager().set(RegisterAll.LURE_EGG_ITEM, 20);
            case "target_egg" -> user.getItemCooldownManager().set(RegisterAll.TARGET_EGG_ITEM, 50);
            case "hermes_egg" -> user.getItemCooldownManager().set(RegisterAll.HERMES_EGG_ITEM, 10);
            case "vortex_egg" -> user.getItemCooldownManager().set(RegisterAll.VORTEX_EGG_ITEM, 300);
            case "experience_egg" -> user.getItemCooldownManager().set(RegisterAll.EXPERIENCE_EGG_ITEM, 10);
            case "capture_egg" -> user.getItemCooldownManager().set(RegisterAll.CAPTURE_EGG_ITEM, 10);
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
