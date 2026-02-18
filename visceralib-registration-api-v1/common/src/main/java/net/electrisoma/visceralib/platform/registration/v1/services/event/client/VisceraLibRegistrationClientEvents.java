package net.electrisoma.visceralib.platform.registration.v1.services.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.registration.v1.client.ParticleRegistrationNamespaced;


public interface VisceraLibRegistrationClientEvents {

	VisceraLibRegistrationClientEvents INSTANCE = ServiceHelper.load(VisceraLibRegistrationClientEvents.class);

	void registerParticleProviders(ParticleRegistrationNamespaced handler);
}
