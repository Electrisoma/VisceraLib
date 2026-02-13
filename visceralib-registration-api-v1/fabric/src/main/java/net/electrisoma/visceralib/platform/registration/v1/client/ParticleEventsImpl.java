package net.electrisoma.visceralib.platform.registration.v1.client;

import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleEvents;
import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleRegistrationHelper;

import com.google.auto.service.AutoService;

import java.util.function.Consumer;

@AutoService(IParticleEvents.class)
public class ParticleEventsImpl implements IParticleEvents {

	@Override
	public void registerProviders(Consumer<IParticleRegistrationHelper> consumer) {
		consumer.accept(new ParticleRegistrationImpl());
	}
}
