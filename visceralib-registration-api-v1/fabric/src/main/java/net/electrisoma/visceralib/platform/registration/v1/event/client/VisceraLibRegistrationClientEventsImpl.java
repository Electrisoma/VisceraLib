package net.electrisoma.visceralib.platform.registration.v1.event.client;

import net.electrisoma.visceralib.event.registration.v1.client.ParticleRegistrar;
import net.electrisoma.visceralib.event.registration.v1.client.ParticleRegistrationNamespaced;
import net.electrisoma.visceralib.platform.registration.v1.services.event.client.VisceraLibRegistrationClientEvents;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import com.google.auto.service.AutoService;

import java.util.function.Consumer;

@AutoService(VisceraLibRegistrationClientEvents.class)
public final class VisceraLibRegistrationClientEventsImpl implements VisceraLibRegistrationClientEvents {

	@Override
	public void registerParticleProviders(ParticleRegistrationNamespaced handler) {
		handler.register(new ParticleRegistrar() {

			@Override
			public <T extends ParticleOptions> void registerSpecial(
					ParticleType<T> type,
					ParticleProvider<T> provider
			) {
				ParticleFactoryRegistry.getInstance().register(type, provider);
			}

			@Override
			public <T extends ParticleOptions> void registerSpriteSet(
					ParticleType<T> type,
					ParticleEngine.SpriteParticleRegistration<T> registration
			) {
				ParticleFactoryRegistry.getInstance().register(type, registration::create);
			}
		});
	}
}
