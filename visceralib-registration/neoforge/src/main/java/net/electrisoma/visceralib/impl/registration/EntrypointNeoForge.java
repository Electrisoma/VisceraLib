package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.Registration;
import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class EntrypointNeoForge {

    public EntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(EntrypointNeoForge::init);
        modEventBus.addListener(ModBusEvents::onRegister);
    }

    public static void init(final FMLCommonSetupEvent event) {
        Entrypoint.init();
    }

    private static class ModBusEvents {

        public static void onRegister(RegisterEvent event) {
            for (Registration<?, ?, ?> registration : VisceralRegistry.REGISTRATIONS_VIEW.get(event.getRegistry())) {
                if (registration != null)
                    registration.register();
            }
        }
    }
}
