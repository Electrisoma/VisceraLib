package net.electrisoma.visceralib.fabric;

import net.electrisoma.visceralib.VisceraLib;

import net.electrisoma.visceralib.api.fabric.registration.VisceralBootstrapFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class VisceraLibImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		VisceralBootstrapFabric.init();

		VisceraLib.init();
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> CreativeTabFillerFabric.register());
	}
}
