package net.ozbozmodz.eggmod.advancements;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EggAdvancementProvider extends FabricAdvancementProvider {
    public EggAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry root = Advancement.Builder.create()
                .display(
                        Items.EGG,
                        Text.literal("The World of Eggs"),
                        Text.literal("Oh the eggs you will make!"),
                        Identifier.of("eggmod", "textures/block/eggshell_bricks.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        true
                )
                .criterion("root", InventoryChangedCriterion.Conditions.items(Items.EGG))
                .build(consumer, "eggmod/root");

        AdvancementEntry getEggshell = Advancement.Builder.create()
                .parent(root)
                .display(
                        RegisterAll.EGGSHELL_ITEM,
                        Text.literal("An Empty Shell"),
                        Text.literal("Collect an Eggshell"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("get_eggshell", InventoryChangedCriterion.Conditions.items(RegisterAll.EGGSHELL_ITEM))
                .build(consumer, "eggmod/get_eggshell");

        AdvancementEntry burntEgg = Advancement.Builder.create()
                .parent(root)
                .display(
                        RegisterAll.BURNT_EGG_ITEM,
                        Text.literal("Kitchen Mishap"),
                        Text.literal("Cook an egg in a furnace, realize why you shouldn't cook an egg in a furnace"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion("get_burnt_egg", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, "eggmod/get_burnt_egg");

        AdvancementEntry getMysteriousEgg = Advancement.Builder.create()
                .parent(root)
                .display(
                        RegisterAll.MYSTERIOUS_EGG_ITEM,
                        Text.literal("Out of Thin Air"),
                        Text.literal("Discover a Mysterious Egg"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("get_mystery", InventoryChangedCriterion.Conditions.items(RegisterAll.MYSTERIOUS_EGG_ITEM))
                .build(consumer, "eggmod/get_mystery");

        AdvancementEntry eggshellArmor = Advancement.Builder.create()
                .parent(getEggshell)
                .display(
                        RegisterAll.EGGSHELL_CHESTPLATE,
                        Text.literal("Invincible"),
                        Text.literal("Fully absorb a hit of damage with Eggshell Armor"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("absorb_damage", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
                .build(consumer, "eggmod/absorb_damage");

        AdvancementEntry giantEgg = Advancement.Builder.create()
                .parent(root)
                .display(
                        RegisterAll.GIANT_EGG_ITEM,
                        Text.literal("Full Course Meal"),
                        Text.literal("Cook a Giant Egg, the right way"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("get_giant_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.GIANT_EGG_ITEM))
                .build(consumer, "eggmod/get_giant_egg");

        AdvancementEntry etcher = Advancement.Builder.create()
                .parent(root)
                .display(
                        RegisterAll.ETCHER_ITEM,
                        Text.literal("Straight up Etchin' it"),
                        Text.literal("Craft an Etching Station"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("get_etcher", InventoryChangedCriterion.Conditions.items(RegisterAll.ETCHER_ITEM))
                .build(consumer, "eggmod/get_etcher");

        AdvancementEntry allEggs = Advancement.Builder.create()
                .parent(etcher)
                .display(
                        RegisterAll.EXCAVATOR_EGG_ITEM,
                        Text.literal("Eggs Galore"),
                        Text.literal("Gather every type of egg"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("blast_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.BLAST_EGG_ITEM))
                .criterion("iron_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.IRON_EGG_ITEM))
                .criterion("diamond_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.DIAMOND_EGG_ITEM))
                .criterion("excavator_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.EXCAVATOR_EGG_ITEM))
                .criterion("recall_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.RECALL_EGG_ITEM))
                .criterion("capture_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.CAPTURE_EGG_ITEM))
                .criterion("experience_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.EXPERIENCE_EGG_ITEM))
                .criterion("hermes_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.HERMES_EGG_ITEM))
                .criterion("lure_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.LURE_EGG_ITEM))
                .criterion("overclock_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.OVERCLOCK_EGG_ITEM))
                .criterion("plaster_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.PLASTER_EGG_ITEM))
                .criterion("sponge_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.SPONGE_EGG_ITEM))
                .criterion("target_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.TARGET_EGG_ITEM))
                .criterion("vortex_egg", InventoryChangedCriterion.Conditions.items(RegisterAll.VORTEX_EGG_ITEM))
                .build(consumer, "eggmod/all_eggs");

        AdvancementEntry eggsBasket = Advancement.Builder.create()
                .parent(etcher)
                .display(
                        RegisterAll.EGGS_IN_A_BASKET_ITEM.getDefaultStack(),
                        Text.literal("Basketeer"),
                        Text.literal("Craft Eggs in a Basket"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("get_basket", InventoryChangedCriterion.Conditions.items(RegisterAll.EGGS_IN_A_BASKET_ITEM))
                .build(consumer, "eggmod/get_basket");
    }
}
