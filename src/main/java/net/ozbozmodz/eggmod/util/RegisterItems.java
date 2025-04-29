package net.ozbozmodz.eggmod.util;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.ozbozmodz.eggmod.blocks.*;
import net.ozbozmodz.eggmod.fooditems.*;
import net.ozbozmodz.eggmod.gadgets.specialSyringe;
import net.ozbozmodz.eggmod.throwableEggs.damageEgg;

public class RegisterItems {
    //ENTITIES
    
    //FOOD ITEMS
    public static final burntEgg BURNTEGG = new burntEgg(new Item.Settings().food(modFoodComps.BURNTEGG).maxCount(16));
    public static final Item FRIEDEGG = new Item(new Item.Settings().food(modFoodComps.FRIEDEGG));
    //MISC ITEMS
    public static final specialSyringe SPECIALSYRINGE = new specialSyringe(new Item.Settings());
    public static final Item DRAGONSERUM = new Item(new Item.Settings().rarity(Rarity.EPIC));
    //public static final Eggzooka EGGZOOKA = new Eggzooka(new Item.Settings().maxCount(1));
    //THROWABLE EGGS
    public static final damageEgg BLASTEGG = new damageEgg(new Item.Settings(), "BLASTEGG");
    //BLOCKS
    public static final eggshells EGGSHELLS = new eggshells(Block.Settings.copy(Blocks.WHITE_CARPET).strength(0.5f).nonOpaque().sounds(BlockSoundGroup.TUFF).strength(0.2F));
    public static final giantEgg GIANTEGG = new giantEgg(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.OFF_WHITE));
    public static final rawGiantEgg RAWGIANTEGG = new rawGiantEgg(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.OFF_WHITE));
    //BLOCK ITEMS
    public static final Item EGGSHELLITEM = new BlockItem(EGGSHELLS, new Item.Settings());
    public static final Item GIANTEGGITEM = new BlockItem(GIANTEGG, new Item.Settings());
    public static final Item RAWGIANTEGGITEM = new BlockItem(RAWGIANTEGG, new Item.Settings());
    //BEHAVIORS
    public static final eggshellDispenserBehavior EGGSHELL_DISPENSER_BEHAVIOR = new eggshellDispenserBehavior();
    //ITEMGROUP
    public static final ItemGroup Eggmod = Registry.register(Registries.ITEM_GROUP, Identifier.of("eggmod", "general"),
    FabricItemGroup.builder()
        .icon(() -> new ItemStack(BURNTEGG))
        .displayName(Text.literal("Eggmod!"))
        .entries((displayContext, entries) ->{
            entries.add(BURNTEGG);
            entries.add(FRIEDEGG);
            entries.add(DRAGONSERUM);
            entries.add(SPECIALSYRINGE);
            entries.add(EGGSHELLITEM);
            entries.add(GIANTEGGITEM);
            entries.add(RAWGIANTEGGITEM);
        }).build());

    public static void Register(){
        //Food Items
        Registry.register(Registries.ITEM, Identifier.of("eggmod","burnt_egg"), BURNTEGG);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "fried_egg"), FRIEDEGG);

        //Misc Items
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "special_syringe"), SPECIALSYRINGE);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "dragon_serum"), DRAGONSERUM);

        //Blocks
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "eggshells"), EGGSHELLS);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "eggshells"), EGGSHELLITEM);
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "giant_egg"), GIANTEGG);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "giant_egg"), GIANTEGGITEM);
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "raw_giant_egg"), RAWGIANTEGG);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "raw_giant_egg"), RAWGIANTEGGITEM);

        //Gadgets
        //Registry.register(Registry.ITEM, Identifier.of("eggmod", "eggzooka"), EGGZOOKA);

        //Throwable Eggs
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "blast_egg"), BLASTEGG);

        //Behaviors
        DispenserBlock.registerBehavior(RegisterItems.EGGSHELLITEM, RegisterItems.EGGSHELL_DISPENSER_BEHAVIOR);
    }
}
