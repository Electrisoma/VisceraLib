package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.register.VisceralRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;

@Mod(Constants.MOD_ID)
public class EntrypointNeoForge {

    public EntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Entrypoint.init();
        modEventBus.addListener(RegistrationEvents::onRegister);
    }

    public static class RegistrationEvents {

        public static void onRegister(RegisterEvent event) {
            var registrations = VisceralRegistry.REGISTRATIONS_VIEW.get(event.getRegistryKey());
            registrations.stream().filter(Objects::nonNull).forEach(VisceralRegistry.Registration::register);
        }
    }
}