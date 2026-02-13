package net.electrisoma.visceralib.impl.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.registration.v1.services.IDynamicRegistryHelper;
import net.electrisoma.visceralib.platform.registration.v1.services.INewRegistryHelper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;

@Mod(Constants.MOD_ID)
public class EntrypointNeoForge {

	public EntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
		IPlatformHelper.INSTANCE.registerModBus(modEventBus);
		Entrypoint.init();
		modEventBus.addListener(RegistrationEvents::onRegister);
		modEventBus.addListener(RegistrationEvents::onNewRegistry);
		modEventBus.addListener(RegistrationEvents::onNewDatapackRegistry);
	}

	public static class RegistrationEvents {

		public static void onRegister(RegisterEvent event) {
			var registrations = VisceralRegistry.ENTRIES_VIEW.get(event.getRegistryKey());
			registrations.stream().filter(Objects::nonNull).forEach(VisceralRegistry.Registration::register);
		}

		public static void onNewRegistry(NewRegistryEvent event) {
			INewRegistryHelper.INSTANCE.onNewRegistry(event);
		}

		public static void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
			IDynamicRegistryHelper.INSTANCE.onNewDatapackRegistry(event);
		}
	}
}
