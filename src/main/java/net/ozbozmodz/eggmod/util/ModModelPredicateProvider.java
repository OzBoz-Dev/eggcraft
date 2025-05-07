package net.ozbozmodz.eggmod.util;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.Identifier;
import net.ozbozmodz.eggmod.screen.EtcherBlockScreen;

public class ModModelPredicateProvider {
    public static void registerModModels(){
        ModelPredicateProviderRegistry.register(RegisterItems.SPECIAL_SYRINGE_ITEM, Identifier.of("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 60.0f;
        });
        ModelPredicateProviderRegistry.register(RegisterItems.SPECIAL_SYRINGE_ITEM, Identifier.of("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        // Entity renderers
        EntityRendererRegistry.register(RegisterItems.BLAST_EGG_ENTITY_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterItems.IRON_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterItems.DIAMOND_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterItems.EXCAVATOR_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);

        // Screen renderers
        HandledScreens.register(RegisterItems.ETCHER_BLOCK_SCREEN_HANDLER, EtcherBlockScreen::new);
    }
}
