package net.electrisoma.visceralib.multiloader.fabric;

import net.electrisoma.visceralib.api.fabric.registration.VisceralDeferredRegisterFabric;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;

public class VisceralRegistrySetupImpl {
    public static VisceralRegistries.DeferredRegisterFactory createFactory() {
        return VisceralDeferredRegisterFabric::new;
    }
}