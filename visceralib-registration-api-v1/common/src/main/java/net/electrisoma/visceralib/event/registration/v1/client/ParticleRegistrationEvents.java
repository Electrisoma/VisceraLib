package net.electrisoma.visceralib.event.registration.v1.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.event.core.client.IVisceralClientListener;
import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleEvents;
import net.electrisoma.visceralib.platform.registration.v1.client.services.IParticleRegistrationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@AutoService(IVisceralClientListener.class)
public class ParticleRegistrationEvents implements IVisceralClientListener {

    private static final List<Consumer<IParticleRegistrationHelper>> LOOKUP = new ArrayList<>();

    @Override
    public void register() {
        IParticleEvents.INSTANCE.registerProviders(helper ->
            LOOKUP.forEach(registrator -> registrator.accept(helper))
        );
    }

    public static void registerFactories(Consumer<IParticleRegistrationHelper> registrator) {
        LOOKUP.add(registrator);
    }
}