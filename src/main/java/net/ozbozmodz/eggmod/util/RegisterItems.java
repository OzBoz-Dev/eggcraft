package net.ozbozmodz.eggmod.util;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.ozbozmodz.eggmod.blocks.*;
import net.ozbozmodz.eggmod.entities.EtcherBlockEntity;
import net.ozbozmodz.eggmod.fooditems.*;
import net.ozbozmodz.eggmod.items.Eggzooka;
import net.ozbozmodz.eggmod.items.SpecialSyringeItem;
import net.ozbozmodz.eggmod.items.TemplateItem;
import net.ozbozmodz.eggmod.screen.EtcherBlockScreenHandler;
import net.ozbozmodz.eggmod.throwableEggs.*;

import java.util.List;

public class RegisterItems {
    // COMPONENTS
    public static final ComponentType<List<ItemStack>> EGG_INV = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of("eggmod", "egg_inv"),
            ComponentType.<List<ItemStack>>builder().codec(Codec.list(ItemStack.CODEC)).build());

    // ENTITIES
    
    // FOOD ITEMS
    public static final BurntEggItem BURNT_EGG_ITEM = new BurntEggItem(new Item.Settings().food(ModFoodComps.BURNTEGG).maxCount(16));
    public static final Item FRIED_EGG_ITEM = new Item(new Item.Settings().food(ModFoodComps.FRIEDEGG));

    // MISC ITEMS
    public static final SpecialSyringeItem SPECIAL_SYRINGE_ITEM = new SpecialSyringeItem(new Item.Settings());
    public static final Item ENDER_SERUM_ITEM = new Item(new Item.Settings().rarity(Rarity.EPIC).maxCount(1));

    // TEMPLATE ITEMS
    public static final TemplateItem BLANK_TEMPLATE = new TemplateItem(new Item.Settings());
    public static final TemplateItem BLAST_EGG_TEMPLATE = new TemplateItem(new Item.Settings());
    public static final TemplateItem IRON_EGG_TEMPLATE = new TemplateItem(new Item.Settings());
    public static final TemplateItem DIAMOND_EGG_TEMPLATE = new TemplateItem(new Item.Settings());
    public static final TemplateItem EXCAVATOR_EGG_TEMPLATE = new TemplateItem(new Item.Settings());
    public static final TemplateItem SPONGE_EGG_TEMPLATE = new TemplateItem(new Item.Settings());

    // THROWABLE EGGS
    public static final CustomEggItem BLAST_EGG_ITEM = new CustomEggItem(new Item.Settings());
    public static final CustomEggItem IRON_EGG_ITEM = new CustomEggItem(new Item.Settings());
    public static final CustomEggItem DIAMOND_EGG_ITEM = new CustomEggItem(new Item.Settings());
    public static final CustomEggItem EXCAVATOR_EGG_ITEM = new CustomEggItem(new Item.Settings());
    public static final CustomEggItem SPONGE_EGG_ITEM = new CustomEggItem(new Item.Settings());
    public static final EntityType<BlastEggEntity> BLAST_EGG_ENTITY_ENTITY_TYPE = EntityType.Builder.<BlastEggEntity>create(BlastEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(4).makeFireImmune().build();
    public static final EntityType<IronEggEntity> IRON_EGG_ENTITY_TYPE = EntityType.Builder.<IronEggEntity>create(IronEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();
    public static final EntityType<DiamondEggEntity> DIAMOND_EGG_ENTITY_TYPE = EntityType.Builder.<DiamondEggEntity>create(DiamondEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();
    public static final EntityType<ExcavatorEggEntity> EXCAVATOR_EGG_ENTITY_TYPE = EntityType.Builder.<ExcavatorEggEntity>create(ExcavatorEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();
    public static final EntityType<ExcavatorEggEntity> SPONGE_EGG_ENTITY_TYPE = EntityType.Builder.<ExcavatorEggEntity>create(ExcavatorEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build();

    public static final Eggzooka EGGZOOKA = new Eggzooka(new Item.Settings().maxCount(1).component(EGG_INV, Eggzooka.defaultList()));
    // BLOCKS
    public static final EggshellBlock EGGSHELL_BLOCK = new EggshellBlock(Block.Settings.copy(Blocks.WHITE_CARPET).strength(0.5f).nonOpaque().sounds(BlockSoundGroup.TUFF).strength(0.2F));
    public static final GiantEggBlock GIANT_EGG_BLOCK = new GiantEggBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.OFF_WHITE));
    public static final RawGiantEggBlock RAW_GIANT_EGG_BLOCK= new RawGiantEggBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.OFF_WHITE));
    public static final EtcherBlock ETCHER_BLOCK = new EtcherBlock(AbstractBlock.Settings.create().nonOpaque().luminance(EtcherBlock::getLuminance).strength(5.0F, 6.0F));
    public static final MysteriousEggBlock MYSTERIOUS_EGG_BLOCK = new MysteriousEggBlock(AbstractBlock.Settings.create().nonOpaque());
    // BLOCK ITEMS
    public static final Item EGGSHELL_ITEM = new BlockItem(EGGSHELL_BLOCK, new Item.Settings());
    public static final Item GIANT_EGG_ITEM = new BlockItem(GIANT_EGG_BLOCK, new Item.Settings());
    public static final Item RAW_GIANT_EGG_ITEM = new BlockItem(RAW_GIANT_EGG_BLOCK, new Item.Settings());
    public static final Item ETCHER_ITEM = new BlockItem(ETCHER_BLOCK, new Item.Settings());
    public static final Item MYSTERIOUS_EGG_ITEM = new BlockItem(MYSTERIOUS_EGG_BLOCK, new Item.Settings());
    // BLOCK ENTITIES
    public static final BlockEntityType<EtcherBlockEntity> ETCHER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("eggmod", "eggtcher_block_entity"),
            BlockEntityType.Builder.create(EtcherBlockEntity::new, RegisterItems.ETCHER_BLOCK).build());

    // SCREEN
    public static final ScreenHandlerType<EtcherBlockScreenHandler> ETCHER_BLOCK_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of("eggmod", "etcher_screen_handler"),
                    new ExtendedScreenHandlerType<>(EtcherBlockScreenHandler::new, BlockPos.PACKET_CODEC));

    // BEHAVIORS
    public static final EggshellDispenserBehavior EGGSHELL_DISPENSER_BEHAVIOR = new EggshellDispenserBehavior();

    // ITEM GROUP
    public static final ItemGroup EGGMOD = Registry.register(Registries.ITEM_GROUP, Identifier.of("eggmod", "general"),
    FabricItemGroup.builder()
        .icon(() -> new ItemStack(BURNT_EGG_ITEM))
        .displayName(Text.literal("Eggmod!"))
        .entries((displayContext, entries) ->{
            entries.add(BURNT_EGG_ITEM);
            entries.add(FRIED_EGG_ITEM);
            entries.add(ENDER_SERUM_ITEM);
            entries.add(SPECIAL_SYRINGE_ITEM);
            entries.add(EGGZOOKA);
            entries.add(EGGSHELL_ITEM);
            entries.add(GIANT_EGG_ITEM);
            entries.add(RAW_GIANT_EGG_ITEM);
            entries.add(ETCHER_ITEM);
            entries.add(BLAST_EGG_ITEM);
            entries.add(IRON_EGG_ITEM);
            entries.add(DIAMOND_EGG_ITEM);
            entries.add(EXCAVATOR_EGG_ITEM);
            entries.add(BLANK_TEMPLATE);
            entries.add(BLAST_EGG_TEMPLATE);
            entries.add(IRON_EGG_TEMPLATE);
            entries.add(DIAMOND_EGG_TEMPLATE);
            entries.add(EXCAVATOR_EGG_TEMPLATE);
            entries.add(MYSTERIOUS_EGG_ITEM);
            entries.add(SPONGE_EGG_ITEM);
            entries.add(SPONGE_EGG_TEMPLATE);
        }).build());

    public static void Register(){
        // Food Items
        Registry.register(Registries.ITEM, Identifier.of("eggmod","burnt_egg"), BURNT_EGG_ITEM);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "fried_egg"), FRIED_EGG_ITEM);

        // Misc Items
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "special_syringe"), SPECIAL_SYRINGE_ITEM);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "ender_serum"), ENDER_SERUM_ITEM);

        // Template Items
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "templates/blank_template"), BLANK_TEMPLATE);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "templates/blast_egg_template"), BLAST_EGG_TEMPLATE);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "templates/iron_egg_template"), IRON_EGG_TEMPLATE);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "templates/diamond_egg_template"), DIAMOND_EGG_TEMPLATE);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "templates/excavator_egg_template"), EXCAVATOR_EGG_TEMPLATE);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "templates/sponge_egg_template"), SPONGE_EGG_TEMPLATE);

        // Blocks
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "eggshells"), EGGSHELL_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "eggshells"), EGGSHELL_ITEM);
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "giant_egg"), GIANT_EGG_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "giant_egg"), GIANT_EGG_ITEM);
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "raw_giant_egg"), RAW_GIANT_EGG_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "raw_giant_egg"), RAW_GIANT_EGG_ITEM);
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "etcher_block"), ETCHER_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "etcher_block"), ETCHER_ITEM);
        Registry.register(Registries.BLOCK, Identifier.of("eggmod", "mysterious_egg"), MYSTERIOUS_EGG_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "mysterious_egg"), MYSTERIOUS_EGG_ITEM);

        // Gadgets
         Registry.register(Registries.ITEM, Identifier.of("eggmod", "eggzooka"), EGGZOOKA);

        // Throwable Egg
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "blast_egg"), BLAST_EGG_ITEM);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "iron_egg"), IRON_EGG_ITEM);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "diamond_egg"), DIAMOND_EGG_ITEM);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "excavator_egg"), EXCAVATOR_EGG_ITEM);
        Registry.register(Registries.ITEM, Identifier.of("eggmod", "sponge_egg"), SPONGE_EGG_ITEM);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "blast_egg_entity"), BLAST_EGG_ENTITY_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "iron_egg_entity"), IRON_EGG_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "diamond_egg_entity"), DIAMOND_EGG_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "excavator_egg_entity"), EXCAVATOR_EGG_ENTITY_TYPE);
        Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", "excavator_egg_entity"), SPONGE_EGG_ENTITY_TYPE);

        // Behaviors
        DispenserBlock.registerBehavior(RegisterItems.EGGSHELL_ITEM, RegisterItems.EGGSHELL_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(RegisterItems.BLAST_EGG_ITEM, new CustomEggDispenserBehavior(BLAST_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterItems.IRON_EGG_ITEM, new CustomEggDispenserBehavior(IRON_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterItems.DIAMOND_EGG_ITEM, new CustomEggDispenserBehavior(DIAMOND_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterItems.EXCAVATOR_EGG_ITEM, new CustomEggDispenserBehavior(EXCAVATOR_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterItems.SPONGE_EGG_ITEM, new CustomEggDispenserBehavior(SPONGE_EGG_ITEM));
    }
}
