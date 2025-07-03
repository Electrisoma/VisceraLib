package net.electrisoma.visceralib.multiloader.forge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.forge.registration.VisceralDeferredRegisterForge;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.minecraftforge.eventbus.api.IEventBus;


public class VisceralRegistrySetupImpl {
    public static VisceralRegistries.DeferredRegisterFactory createFactory() {
        return VisceralDeferredRegisterForge::new;
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
