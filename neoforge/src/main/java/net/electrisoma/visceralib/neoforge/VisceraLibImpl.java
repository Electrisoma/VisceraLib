package net.electrisoma.visceralib.neoforge;

import net.electrisoma.visceralib.VisceraLib;

import net.electrisoma.visceralib.api.neoforge.registration.VisceralBootstrapNeoForge;
import net.electrisoma.visceralib.api.neoforge.registration.VisceralDeferredRegisterNeoForge;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.TabBuilder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(VisceraLib.MOD_ID)
public class VisceraLibImpl {
	static IEventBus eventBus;
	static IEventBus neoforgeBus;

	public VisceraLibImpl() {
		eventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		neoforgeBus = NeoForge.EVENT_BUS;

		VisceraLib.init();
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		for (BlockBuilder<?, ?> builder : BlockBuilder.getAllBuilders())
			builder.getRegisteredSupplier().ifPresent(VisceralRegistrySupplier::notifyListeners);
	}
}
