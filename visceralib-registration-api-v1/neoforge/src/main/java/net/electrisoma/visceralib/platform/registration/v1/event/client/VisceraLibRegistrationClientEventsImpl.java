package net.electrisoma.visceralib.platform.registration.v1.event.client;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.event.registration.v1.client.ParticleRegistrar;
import net.electrisoma.visceralib.event.registration.v1.client.ParticleRegistrationNamespaced;
import net.electrisoma.visceralib.platform.registration.v1.services.event.client.VisceraLibRegistrationClientEvents;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibRegistrationClientEvents.class)
public final class VisceraLibRegistrationClientEventsImpl implements VisceraLibRegistrationClientEvents, IEventBusHelper {

	@Override
	public void registerParticleProviders(ParticleRegistrationNamespaced handler) {
		withModBus(bus -> bus.addListener((RegisterParticleProvidersEvent event) ->
				handler.register(new ParticleRegistrar() {

					@Override
					public <T extends ParticleOptions> void registerSpecial(
							ParticleType<T> type,
							ParticleProvider<T> provider
					) {
						event.registerSpecial(type, provider);
					}

					@Override
					public <T extends ParticleOptions> void registerSpriteSet(
							ParticleType<T> type,
							ParticleEngine.SpriteParticleRegistration<T> registration
					) {
						event.registerSpriteSet(type, registration);
					}
				})
		));
	}
}
