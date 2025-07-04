package net.electrisoma.visceralib.multiloader.forge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.forge.registration.VisceralDeferredRegisterForge;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.minecraftforge.eventbus.api.IEventBus;

public class VisceralRegistrySetupImpl {
    public static VisceralRegistries.DeferredRegisterFactory createFactory() {
        return VisceralDeferredRegisterForge::new;
    }

    public static void registerToEventBus(Object eventBus, String modId) {
        if (!(eventBus instanceof IEventBus bus))
            throw new IllegalArgumentException("Expected Forge IEventBus");
        for (VisceralDeferredRegister<?> deferred : VisceralRegistries.getAllForMod(VisceraLib.MOD_ID)) // i dont like this
            deferred.registerToEventBus(bus);
    }
}
