package net.electrisoma.visceralib.testreg.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class TestParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public TestParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                   double x, double y, double z,
                                   double dx, double dy, double dz) {
        return new TestCloudParticle(level, x, y, z, dx, dy, dz, sprites);
    }
}
