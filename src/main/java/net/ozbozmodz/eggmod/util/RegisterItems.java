package net.ozbozmodz.eggmod.util;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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
import net.ozbozmodz.eggmod.throwableEggs.*;

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
    public static final customEggItem BLASTEGG = new customEggItem(new Item.Settings(), "BLASTEGG");
    public static final customEggItem IRONEGG = new customEggItem(new Item.Settings(), "IRONEGG");
    public static final customEggItem DIAMONDEGG = new customEggItem(new Item.Settings(), "DIAMONDEGG");
    public static final customEggItem EXCAVATOREGG = new customEggItem(new Item.Settings(), "EXCAVATOREGG");
    public static final EntityType<blastEggEntity> BLAST_EGG_ENTITY_ENTITY_TYPE = EntityType.Builder.<blastEggEntity>create(blastEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(4).makeFireImmune().build();
    public static final EntityType<ironEggEntity> IRON_EGG_ENTITY_TYPE = EntityType.Builder.<ironEggEntity>create(ironEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();
    public static final EntityType<diamondEggEntity> DIAMOND_EGG_ENTITY_TYPE = EntityType.Builder.<diamondEggEntity>create(diamondEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();
    public static final EntityType<excavatorEggEntity> EXCAVATOR_EGG_ENTITY_TYPE = EntityType.Builder.<excavatorEggEntity>create(excavatorEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();
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
            entries.add(BLASTEGG);
            entries.add(IRONEGG);
            entries.add(DIAMONDEGG);
            entries.add(EXCAVATOREGG);
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

        //Throwable Egg
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "blast_egg"), BLASTEGG);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "iron_egg"), IRONEGG);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "diamond_egg"), DIAMONDEGG);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "excavator_egg"), EXCAVATOREGG);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "blast_egg_entity"), BLAST_EGG_ENTITY_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "iron_egg_entity"), IRON_EGG_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "diamond_egg_entity"), DIAMOND_EGG_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "excavator_egg_entity"), EXCAVATOR_EGG_ENTITY_TYPE);

        //Behaviors
        DispenserBlock.registerBehavior(RegisterItems.EGGSHELLITEM, RegisterItems.EGGSHELL_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(RegisterItems.BLASTEGG, new customEggDispenserBehavior(BLASTEGG));
        DispenserBlock.registerBehavior(RegisterItems.IRONEGG, new customEggDispenserBehavior(IRONEGG));
        DispenserBlock.registerBehavior(RegisterItems.DIAMONDEGG, new customEggDispenserBehavior(DIAMONDEGG));
        DispenserBlock.registerBehavior(RegisterItems.EXCAVATOREGG, new customEggDispenserBehavior(EXCAVATOREGG));
    }
}
