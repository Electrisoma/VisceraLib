package net.electrisoma.visceralib.api.neoforge.registration;

import net.neoforged.bus.api.IEventBus;

public class VisceralBootstrapNeoForge {
    private static boolean initialized = false;

    public static void init(IEventBus eventBus) {
        if (initialized) return;
        initialized = true;

        VisceralDeferredRegisterNeoForge.setEventBus(eventBus);
    }
}
