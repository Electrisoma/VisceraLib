package net.electrisoma.visceralib.platform.registration.v1.client.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;

import java.util.function.Consumer;

public interface IParticleEvents {

    IParticleEvents INSTANCE = ServiceHelper.load(IParticleEvents.class);

    void registerProviders(Consumer<IParticleRegistrationHelper> consumer);
}
