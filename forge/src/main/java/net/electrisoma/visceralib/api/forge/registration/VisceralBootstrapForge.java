package net.electrisoma.visceralib.api.forge.registration;

import net.minecraftforge.eventbus.api.IEventBus;

public class VisceralBootstrapForge {
    private static boolean initialized = false;

    public static void init(IEventBus eventBus) {
        if (initialized) return;
        initialized = true;

        VisceralDeferredRegisterForge.setEventBus(eventBus);
    }
}
