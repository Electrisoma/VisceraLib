package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.Registration;
import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class EntrypointNeoforge {

    public EntrypointNeoforge(IEventBus modEventBus, ModContainer modContainer) {
        Entrypoint.init();
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void onRegister(RegisterEvent event) {
            for (Registration<?, ?, ?> registration : VisceralRegistry.REGISTRATIONS_VIEW.get(event.getRegistry())) {
                if (registration != null)
                    registration.register();
            }
        }
    }
}
