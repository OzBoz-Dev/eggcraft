package net.ozbozmodz.eggmod.util;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    public static void registerModModels(){
        ModelPredicateProviderRegistry.register(RegisterItems.SPECIALSYRINGE, Identifier.of("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getActiveItem() != stack) {
                return 0.0f;
            }
            return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 60.0f;
        });
        ModelPredicateProviderRegistry.register(RegisterItems.SPECIALSYRINGE, Identifier.of("pulling"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        EntityRendererRegistry.register(RegisterItems.BLAST_EGG_ENTITY_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterItems.IRON_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(RegisterItems.DIAMOND_EGG_ENTITY_TYPE, FlyingItemEntityRenderer::new);
    }
}
