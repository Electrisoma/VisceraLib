package net.electrisoma.visceralib.neoforge;

import net.electrisoma.visceralib.VisceraLib;

import net.electrisoma.visceralib.api.registration.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@Mod(VisceraLib.MOD_ID)
public class VisceraLibImpl {
	static IEventBus eventBus;
	static IEventBus neoforgeBus;

	public VisceraLibImpl() {
		eventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		neoforgeBus = NeoForge.EVENT_BUS;

		VisceraLib.init();
	}

	@EventBusSubscriber(bus = Bus.MOD)
	public static class ModBusEvents {

		@SubscribeEvent
		public static void onRegister(RegisterEvent event) {
			var registrations = VisceralRegistry.REGISTRATIONS_VIEW.get(event.getRegistry());
			for (Registration<?, ?> registration : registrations) {
				registration.register();
			}
		}
	}
}
