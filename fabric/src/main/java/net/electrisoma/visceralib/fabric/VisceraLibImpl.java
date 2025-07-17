package net.electrisoma.visceralib.fabric;

import net.electrisoma.visceralib.VisceraLib;

import net.electrisoma.visceralib.annotations.Env;
import net.electrisoma.visceralib.api.fabric.registration.VisceralBootstrapFabric;
import net.electrisoma.visceralib.api.fabric.registration.VisceralDeferredRegisterFabric;
import net.electrisoma.visceralib.api.registration.VisceralRegistries;
import net.electrisoma.visceralib.api.registration.builders.TabBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class VisceraLibImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		VisceralBootstrapFabric.init();

		VisceraLib.init();
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> CreativeTabFillerFabric.register());
	}
}
