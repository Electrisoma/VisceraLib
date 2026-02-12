package net.electrisoma.visceralib.platform.registration.v1.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleEvents;
import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleRegistrationHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.function.Consumer;

@AutoService(IParticleEvents.class)
public class ParticleEventsImpl implements IParticleEvents {

    @Override
    public void registerProviders(Consumer<IParticleRegistrationHelper> consumer) {
        IEventBus bus = IPlatformHelper.INSTANCE.getModEventBus();
        bus.addListener((RegisterParticleProvidersEvent event) ->
            consumer.accept(new ParticleRegistrationImpl(event))
        );
    }
}
