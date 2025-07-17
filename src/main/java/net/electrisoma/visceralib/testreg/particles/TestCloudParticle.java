package net.electrisoma.visceralib.testreg.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class TestCloudParticle extends TextureSheetParticle {
    public TestCloudParticle(ClientLevel level, double x, double y, double z,
                             double dx, double dy, double dz, SpriteSet spriteSet) {
        super(level, x, y, z, dx, dy, dz);
        this.lifetime = 40 + level.random.nextInt(10);
        this.gravity = 0.0f;
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.setSize(0.5f, 0.5f);
        this.pickSprite(spriteSet);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
