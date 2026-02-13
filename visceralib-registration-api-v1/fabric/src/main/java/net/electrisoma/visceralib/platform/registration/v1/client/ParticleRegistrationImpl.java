package net.electrisoma.visceralib.platform.registration.v1.client;

import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleRegistrationHelper;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public record ParticleRegistrationImpl(
		ParticleFactoryRegistry registry
) implements IParticleRegistrationHelper {

	public ParticleRegistrationImpl() {
		this(ParticleFactoryRegistry.getInstance());
	}

	@Override
	public <T extends ParticleOptions> void register(
			ParticleType<T> type,
			Function<SpriteSet, ParticleProvider<T>> constructor
	) {
		ParticleFactoryRegistry.PendingParticleFactory<T> fabricFactory = constructor::apply;
		registry.register(type, fabricFactory);
	}

	public static IParticleRegistrationHelper getInstance() {
		return new ParticleRegistrationImpl();
	}
}
