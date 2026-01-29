package net.electrisoma.visceralib.platform.registration.v1.client.services;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public interface IParticleRegistrationHelper {

    <T extends ParticleOptions> void register(
            ParticleType<T> type,
            Function<SpriteSet, ParticleProvider<T>> constructor
    );
}