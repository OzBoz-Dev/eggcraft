package net.ozbozmodz.eggmod.render;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.ozbozmodz.eggmod.entities.EtcherBlockEntityRenderer;
import net.ozbozmodz.eggmod.screen.EtcherBlockScreen;
import net.ozbozmodz.eggmod.screen.ExperienceCatcherScreen;
import net.ozbozmodz.eggmod.throwableEggs.CaptureEggItem;
import net.ozbozmodz.eggmod.throwableEggs.ExperienceEggItem;
import net.ozbozmodz.eggmod.util.RegisterAll;

public class RenderHelper {
    public static void registerModModels(){
        ModelPredicateProviderRegistry.register(RegisterAll.SPECIAL_SYRINGE_ITEM, Identifier.of("eggmod","pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 100.0f;
        });
        ModelPredicateProviderRegistry.register(RegisterAll.SPECIAL_SYRINGE_ITEM, Identifier.of("eggmod","pulling"), (stack, world, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);

        ModelPredicateProviderRegistry.register(RegisterAll.EXPERIENCE_EGG_ITEM, Identifier.of("eggmod", "exp"), (stack, world, entity, seed) -> {
            if (!stack.isOf(RegisterAll.EXPERIENCE_EGG_ITEM)) return 0.0f;
            float exp = ExperienceEggItem.getExperience(stack);
            return exp/ExperienceEggItem.maxExperience;
        });

        ModelPredicateProviderRegistry.register(RegisterAll.CAPTURE_EGG_ITEM, Identifier.of("eggmod", "captured"), (stack, world, entity, seed) -> {
            if (!stack.isOf(RegisterAll.CAPTURE_EGG_ITEM)) return 0;
            return CaptureEggItem.getStoredEntity(stack, world) != null ? 1 : 0;
        });

        ModelPredicateProviderRegistry.register(RegisterAll.EGGS_IN_A_BASKET_ITEM, Identifier.of("eggmod", "effect"), (stack, world, entity, seed) -> {
            NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
            if (component == null) return 0;
            NbtCompound compound = component.copyNbt();
            if (compound == null) return 0;
            return (float) (compound.getDouble("effect") / 10);
        });

        // Entity renderers
        EntityRendererRegistry.register(RegisterAll.BLAST_EGG_ENTITY_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.IRON_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.DIAMOND_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.EXCAVATOR_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.SPONGE_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.OVERCLOCK_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.PLASTER_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.LURE_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.TARGET_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.HERMES_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.VORTEX_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.EXPERIENCE_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterAll.CAPTURE_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);


        // Block Rendering
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterAll.ETCHER_BLOCK, RenderLayer.getTranslucent());
        BlockEntityRendererFactories.register(RegisterAll.ETCHER_BLOCK_ENTITY, EtcherBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterAll.EXPERIENCE_CATCHER_BLOCK, RenderLayer.getTranslucent());

        // Screen renderers
        HandledScreens.register(RegisterAll.ETCHER_BLOCK_SCREEN_HANDLER, EtcherBlockScreen::new);
        HandledScreens.register(RegisterAll.EXPERIENCE_CATCHER_SCREEN_HANDLER, ExperienceCatcherScreen::new);
    }
}
