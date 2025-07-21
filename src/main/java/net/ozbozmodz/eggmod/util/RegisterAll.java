package net.ozbozmodz.eggmod.util;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
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

public class RegisterAll {
    // COMPONENTS
    public static final ComponentType<List<ItemStack>> EGG_INV = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of("eggmod", "egg_inv"),
            ComponentType.<List<ItemStack>>builder().codec(Codec.list(ItemStack.CODEC)).build());

    // ENTITIES
    
    // FOOD ITEMS
    public static final Item BURNT_EGG_ITEM = registerItem("burnt_egg", new BurntEggItem(new Item.Settings().food(ModFoodComps.BURNTEGG).maxCount(16)));
    public static final Item FRIED_EGG_ITEM = registerItem("fried_egg", new Item(new Item.Settings().food(ModFoodComps.FRIEDEGG)));

    // MISC ITEMS
    public static final Item SPECIAL_SYRINGE_ITEM = registerItem("special_syringe", new SpecialSyringeItem(new Item.Settings()));
    public static final Item ENDER_SERUM_ITEM = registerItem("ender_serum", new Item(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));
    public static final Item EGGZOOKA = registerItem("eggzooka", new Eggzooka(new Item.Settings().maxCount(1).component(EGG_INV, Eggzooka.defaultList())));

    // TEMPLATE ITEMS
    public static final Item BLANK_TEMPLATE = registerItem("templates/blank_template", new TemplateItem(new Item.Settings()));
    public static final Item BLAST_EGG_TEMPLATE = registerItem("templates/blast_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item IRON_EGG_TEMPLATE = registerItem("templates/iron_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item DIAMOND_EGG_TEMPLATE = registerItem("templates/diamond_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item EXCAVATOR_EGG_TEMPLATE = registerItem("templates/excavator_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item SPONGE_EGG_TEMPLATE = registerItem("templates/sponge_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item OVERCLOCK_EGG_TEMPLATE = registerItem("templates/overclock_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item PLASTER_EGG_TEMPLATE = registerItem("templates/plaster_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item LURE_EGG_TEMPLATE = registerItem("templates/lure_egg_template", new TemplateItem(new Item.Settings()));
    public static final Item TARGET_EGG_TEMPLATE = registerItem("templates/target_egg_template", new TemplateItem(new Item.Settings()));

    // THROWABLE EGGS
    public static final Item BLAST_EGG_ITEM = registerItem("blast_egg", new CustomEggItem(new Item.Settings()));
    public static final Item IRON_EGG_ITEM = registerItem("iron_egg", new CustomEggItem(new Item.Settings()));
    public static final Item DIAMOND_EGG_ITEM = registerItem("diamond_egg", new CustomEggItem(new Item.Settings()));
    public static final Item EXCAVATOR_EGG_ITEM = registerItem("excavator_egg", new CustomEggItem(new Item.Settings()));
    public static final Item SPONGE_EGG_ITEM = registerItem("sponge_egg", new CustomEggItem(new Item.Settings()));
    public static final Item OVERCLOCK_EGG_ITEM = registerItem("overclock_egg", new CustomEggItem(new Item.Settings()));
    public static final Item PLASTER_EGG_ITEM = registerItem("plaster_egg", new CustomEggItem(new Item.Settings()));
    public static final Item LURE_EGG_ITEM = registerItem("lure_egg", new CustomEggItem(new Item.Settings()));
    public static final Item TARGET_EGG_ITEM = registerItem("target_egg", new CustomEggItem(new Item.Settings()));

    // BLOCKS
    public static final Block EGGSHELL_BLOCK = registerBlock("eggshells", new EggshellBlock(Block.Settings.copy(Blocks.WHITE_CARPET).strength(0.5f).nonOpaque().sounds(BlockSoundGroup.TUFF).strength(0.2F)));
    public static final Block GIANT_EGG_BLOCK = registerBlock("giant_egg", new GiantEggBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.OFF_WHITE)));
    public static final Block RAW_GIANT_EGG_BLOCK= registerBlock("raw_giant_egg", new RawGiantEggBlock(AbstractBlock.Settings.copy(Blocks.SLIME_BLOCK).mapColor(MapColor.OFF_WHITE)));
    public static final Block ETCHER_BLOCK = registerBlock("etcher_block", new EtcherBlock(AbstractBlock.Settings.create().nonOpaque().luminance(EtcherBlock::getLuminance).strength(5.0F, 6.0F)));
    public static final Block MYSTERIOUS_EGG_BLOCK = registerBlock("mysterious_egg", new MysteriousEggBlock(AbstractBlock.Settings.create().nonOpaque()));

    // BLOCK ITEMS
    public static final Item EGGSHELL_ITEM = registerItem("eggshells", new BlockItem(EGGSHELL_BLOCK, new Item.Settings()));
    public static final Item GIANT_EGG_ITEM = registerItem("giant_egg", new BlockItem(GIANT_EGG_BLOCK, new Item.Settings()));
    public static final Item RAW_GIANT_EGG_ITEM = registerItem("raw_giant_egg", new BlockItem(RAW_GIANT_EGG_BLOCK, new Item.Settings()));
    public static final Item ETCHER_ITEM = registerItem("etcher_block", new BlockItem(ETCHER_BLOCK, new Item.Settings()));
    public static final Item MYSTERIOUS_EGG_ITEM = registerItem("mysterious_egg", new BlockItem(MYSTERIOUS_EGG_BLOCK, new Item.Settings()));

    // BLOCK ENTITIES
    public static final BlockEntityType<EtcherBlockEntity> ETCHER_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("eggmod", "eggtcher_block_entity"),
            BlockEntityType.Builder.create(EtcherBlockEntity::new, RegisterAll.ETCHER_BLOCK).build());

    // SCREEN
    public static final ScreenHandlerType<EtcherBlockScreenHandler> ETCHER_BLOCK_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of("eggmod", "etcher_screen_handler"),
                    new ExtendedScreenHandlerType<>(EtcherBlockScreenHandler::new, BlockPos.PACKET_CODEC));

    // SOUNDS
    public static final SoundEvent OVERCLOCK_EGG_TICK = registerSoundEvent("eggmod:clock_tick", SoundEvent.of(Identifier.of("eggmod:clock_tick")));

    // BEHAVIORS
    public static final EggshellDispenserBehavior EGGSHELL_DISPENSER_BEHAVIOR = new EggshellDispenserBehavior();

    @SuppressWarnings("unchecked")
    public static final EntityType<BlastEggEntity> BLAST_EGG_ENTITY_ENTITY_TYPE = (EntityType<BlastEggEntity>) registerEntityType("blast_egg_entity", EntityType.Builder.<BlastEggEntity>create(BlastEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(4).makeFireImmune().build());
    @SuppressWarnings("unchecked")
    public static final EntityType<IronEggEntity> IRON_EGG_ENTITY_TYPE = (EntityType<IronEggEntity>) registerEntityType("iron_egg_entity", EntityType.Builder.<IronEggEntity>create(IronEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<DiamondEggEntity> DIAMOND_EGG_ENTITY_TYPE = (EntityType<DiamondEggEntity>) registerEntityType("diamond_egg_entity", EntityType.Builder.<DiamondEggEntity>create(DiamondEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<ExcavatorEggEntity> EXCAVATOR_EGG_ENTITY_TYPE = (EntityType<ExcavatorEggEntity>) registerEntityType("excavator_egg_entity", EntityType.Builder.<ExcavatorEggEntity>create(ExcavatorEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<SpongeEggEntity> SPONGE_EGG_ENTITY_TYPE = (EntityType<SpongeEggEntity>) registerEntityType("sponge_egg_entity", EntityType.Builder.<SpongeEggEntity>create(SpongeEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<OverclockEggEntity> OVERCLOCK_EGG_ENTITY_TYPE = (EntityType<OverclockEggEntity>) registerEntityType("overclock_egg_entity", EntityType.Builder.<OverclockEggEntity>create(OverclockEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<PlasterEggEntity> PLASTER_EGG_ENTITY_TYPE = (EntityType<PlasterEggEntity>) registerEntityType("plaster_egg_entity", EntityType.Builder.<PlasterEggEntity>create(PlasterEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<LureEggEntity> LURE_EGG_ENTITY_TYPE = (EntityType<LureEggEntity>) registerEntityType("lure_egg_entity", EntityType.Builder.<LureEggEntity>create(LureEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());
    @SuppressWarnings("unchecked")
    public static final EntityType<TargetEggEntity> TARGET_EGG_ENTITY_TYPE = (EntityType<TargetEggEntity>) registerEntityType("target_egg_entity", EntityType.Builder.<TargetEggEntity>create(TargetEggEntity::new, SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F).maxTrackingRange(20).trackingTickInterval(20).build());

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
            entries.add(OVERCLOCK_EGG_ITEM);
            entries.add(OVERCLOCK_EGG_TEMPLATE);
            entries.add(PLASTER_EGG_ITEM);
            entries.add(PLASTER_EGG_TEMPLATE);
            entries.add(LURE_EGG_ITEM);
            entries.add(LURE_EGG_TEMPLATE);
            entries.add(TARGET_EGG_ITEM);
            entries.add(TARGET_EGG_TEMPLATE);
        }).build());

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of("eggmod", name), item);
    }

    private static EntityType<? extends Entity> registerEntityType(String name, EntityType<? extends Entity> entityType){
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of("eggmod", name), entityType);
    }

    private static Block registerBlock(String name, Block block){
        return Registry.register(Registries.BLOCK, Identifier.of("eggmod", name), block);
    }

    private static SoundEvent registerSoundEvent(String name, SoundEvent soundEvent){
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(name), soundEvent);
    }

    public static void registerBehaviors(){
        // Behaviors
        DispenserBlock.registerBehavior(RegisterAll.EGGSHELL_ITEM, RegisterAll.EGGSHELL_DISPENSER_BEHAVIOR);
        DispenserBlock.registerBehavior(RegisterAll.BLAST_EGG_ITEM, new CustomEggDispenserBehavior(BLAST_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterAll.IRON_EGG_ITEM, new CustomEggDispenserBehavior(IRON_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterAll.DIAMOND_EGG_ITEM, new CustomEggDispenserBehavior(DIAMOND_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterAll.EXCAVATOR_EGG_ITEM, new CustomEggDispenserBehavior(EXCAVATOR_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterAll.SPONGE_EGG_ITEM, new CustomEggDispenserBehavior(SPONGE_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterAll.OVERCLOCK_EGG_ITEM, new CustomEggDispenserBehavior(OVERCLOCK_EGG_ITEM));
        DispenserBlock.registerBehavior(RegisterAll.PLASTER_EGG_ITEM, new CustomEggDispenserBehavior(PLASTER_EGG_ITEM));
    }
}
