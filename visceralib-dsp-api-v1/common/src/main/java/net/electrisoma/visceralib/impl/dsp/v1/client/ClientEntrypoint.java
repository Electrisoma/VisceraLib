package net.electrisoma.visceralib.impl.dsp.v1.client;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.impl.dsp.v1.Constants;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

public final class ClientEntrypoint {

	public static void init() {
		EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);

		VisceraLibDSPClientEvents.INSTANCE.registerPipelineProvider(context -> {
			if (context.getPlayer() == null || context.getLevel() == null) return;
			if (context.getPlayer().isUnderWater()) {
				context.addPipeline(
						RLUtils.path("testmod", "underwater"),
						1.0f,
						1.0f,
						false
				);
			}}
		);
	}
}
