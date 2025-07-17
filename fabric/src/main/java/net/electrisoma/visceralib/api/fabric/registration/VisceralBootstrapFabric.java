package net.electrisoma.visceralib.api.fabric.registration;

import net.electrisoma.visceralib.api.registration.VisceralRegistries;

public class VisceralBootstrapFabric {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;

        VisceralRegistries.setFactory(VisceralDeferredRegisterFabric::new);
    }
}
