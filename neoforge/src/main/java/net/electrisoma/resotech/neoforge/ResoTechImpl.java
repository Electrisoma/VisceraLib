package net.electrisoma.resotech.neoforge;

import net.electrisoma.resotech.ResoTech;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(ResoTech.MOD_ID)
public class ResoTechImpl {
	static IEventBus eventBus;
	static IEventBus neoforgeBus;

	public ResoTechImpl() {
		eventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
		neoforgeBus = NeoForge.EVENT_BUS;

		ResoTech.init();

		neoforgeBus.addListener(this::onServerStarted);
	}

	public void onServerStarted(ServerStartedEvent event) {
		ResoTech.LOGGER.info(ResoTech.SERVER_START);
	}
}
