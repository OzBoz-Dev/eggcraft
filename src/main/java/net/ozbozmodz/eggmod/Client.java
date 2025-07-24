package net.ozbozmodz.eggmod;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.render.*;
import net.ozbozmodz.eggmod.particles.LockOnParticle;
import net.ozbozmodz.eggmod.util.RegisterAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.ozbozmodz.eggmod.util.ModModelPredicateProvider;


public class Client implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ozutilities");


    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterAll.EGGSHELL_BLOCK, RenderLayer.getCutout());
        ModModelPredicateProvider.registerModModels();
        ParticleFactoryRegistry.getInstance().register(RegisterAll.LOCK_ON_PARTICLE, LockOnParticle.Factory::new);
        LOGGER.info("Client initalized");
    }
}