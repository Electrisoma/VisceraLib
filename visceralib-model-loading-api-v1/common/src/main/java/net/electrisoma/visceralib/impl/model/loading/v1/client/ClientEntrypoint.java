package net.electrisoma.visceralib.impl.model.loading.v1.client;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.model.loading.v1.client.obj.VisceralObjLoader;
import net.electrisoma.visceralib.impl.model.loading.v1.Constants;
import net.electrisoma.visceralib.platform.model.loading.v1.service.event.client.VisceraLibModelEvents;

public final class ClientEntrypoint {

	public static void init() {
		EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
		initializeModelLoaders();
	}

	public static void initializeModelLoaders() {
		VisceraLibModelEvents.INSTANCE.onRegisterGeometryLoaders(event -> {
			event.register(RLUtils.path("visceralib", "obj"), new VisceralObjLoader());
		});
	}
}
