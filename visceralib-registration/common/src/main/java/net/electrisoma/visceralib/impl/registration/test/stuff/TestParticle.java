package net.electrisoma.visceralib.impl.registration.test.stuff;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class TestParticle extends TextureSheetParticle {

    protected TestParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.hasPhysics = true;
        this.quadSize *= 0.75F;
        this.lifetime = 20 + this.random.nextInt(20);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.fadeOut();
    }

    private void fadeOut() {
        this.alpha = 1.0F - ((float)this.age / (float)this.lifetime);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TestParticle(level, x, y, z, sprites);
        }
    }
}