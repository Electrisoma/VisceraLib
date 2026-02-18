package net.electrisoma.visceralib.impl.networking.v1;

import net.fabricmc.api.ModInitializer;

public final class EntrypointFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		Entrypoint.init();
	}
}
