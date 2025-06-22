package net.electrisoma.visceralib.multiloader.neoforge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.neoforge.registration.VisceralDeferredRegisterNeoForge;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.neoforged.bus.api.IEventBus;

public class VisceralRegistrySetupImpl {
    public static VisceralRegistries.DeferredRegisterFactory createFactory() {
        return VisceralDeferredRegisterNeoForge::new;
    }

    public static void registerToEventBus(Object eventBus) {
        if (!(eventBus instanceof IEventBus bus)) {
            throw new IllegalArgumentException("Expected NeoForge IEventBus");
        }

        for (VisceralDeferredRegister<?> deferred : VisceralRegistries.getAllForMod(VisceraLib.MOD_ID)) {
            deferred.registerToEventBus(bus);
        }
    }
}
