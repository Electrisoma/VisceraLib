package net.electrisoma.visceralib.event.registration.v1.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public interface ParticleRegistrar {

	<T extends ParticleOptions> void registerSpecial(
			ParticleType<T> type,
			ParticleProvider<T> provider
	);

	<T extends ParticleOptions> void registerSpriteSet(
			ParticleType<T> type,
			ParticleEngine.SpriteParticleRegistration<T> registration
	);
}
