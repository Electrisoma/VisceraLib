package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;

public final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);

//        VisceralEventBus.subscribe(ParticleRegistrationEvent.class, event -> {
//            event.getHelper().register(TestRegistry.TEST_PARTICLE.get(), TestParticle.Provider::new);
//        });

    }
}
