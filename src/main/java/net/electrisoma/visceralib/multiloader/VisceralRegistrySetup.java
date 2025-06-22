package net.electrisoma.visceralib.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;

public class VisceralRegistrySetup {
    public static void init() {
        VisceralRegistries.setFactory(createFactory());
    }

    @ExpectPlatform
    public static VisceralRegistries.DeferredRegisterFactory createFactory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerToEventBus(Object modEventBus) {
        throw new AssertionError();
    }
}
