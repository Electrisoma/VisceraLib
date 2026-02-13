package net.electrisoma.visceralib.platform.registration.v1.client;

import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleRegistrationHelper;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public record ParticleRegistrationImpl(
		RegisterParticleProvidersEvent event
) implements IParticleRegistrationHelper {

	@Override
	public <T extends ParticleOptions> void register(
			ParticleType<T> type,
			Function<SpriteSet, ParticleProvider<T>> constructor
	) {
		event.registerSpriteSet(type, constructor::apply);
	}

	public static IParticleRegistrationHelper getInstance(RegisterParticleProvidersEvent event) {
		return new ParticleRegistrationImpl(event);
	}
}
