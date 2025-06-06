package net.electrisoma.resotech.fabric;

import net.electrisoma.resotech.ResoTech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ResoTechImpl implements ModInitializer {

	@Override
	public void onInitialize() {
		ResoTech.init();

		onServerStarting();
	}

	public void onServerStarting() {
		ServerLifecycleEvents.SERVER_STARTED.register(server ->
				ResoTech.LOGGER.info(ResoTech.SERVER_START)
		);
	}
}
