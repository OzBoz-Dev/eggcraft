package net.ozbozmodz.eggmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.ozbozmodz.eggmod.util.ModModelPredicateProvider;
import net.ozbozmodz.eggmod.util.RegisterItems;


public class Client implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ozutilities");


    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterItems.EGGSHELLS, RenderLayer.getCutout());
        ModModelPredicateProvider.registerModModels();
        LOGGER.info("Client initalized");
    }
}
