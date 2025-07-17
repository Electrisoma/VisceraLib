package net.electrisoma.visceralib.multiloader.fabric;

import net.electrisoma.visceralib.api.fabric.registration.VisceralBootstrapFabric;

public class VisceralInitHookImpl {
    public static void bootstrap() {
        VisceralBootstrapFabric.init();
    }
}
