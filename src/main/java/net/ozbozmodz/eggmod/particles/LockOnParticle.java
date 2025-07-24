package net.ozbozmodz.eggmod.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class LockOnParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public LockOnParticle(ClientWorld clientWorld, double x, double y, double z,
                          SpriteProvider spriteProvider, double deltaX, double deltaY, double deltaZ) {
        super(clientWorld, x, y, z, deltaX, deltaY, deltaZ);
        this.spriteProvider = spriteProvider;
        this.maxAge = 20;
        this.scale = 0.5f;
        this.velocityMultiplier = 0;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType>{
        private final SpriteProvider spriteProvider;

        // Factory is returned for the particle to render properly
        public Factory(SpriteProvider spriteProvider){
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new LockOnParticle(world, x, y, z, this.spriteProvider, velocityX, velocityY, velocityZ);
        }
    }
}
