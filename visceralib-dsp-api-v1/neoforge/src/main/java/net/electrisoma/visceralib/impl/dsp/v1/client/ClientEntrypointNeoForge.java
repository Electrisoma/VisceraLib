package net.electrisoma.visceralib.impl.dsp.v1.client;

import net.electrisoma.visceralib.api.dsp.v1.DSPPipelineReloadListener;
import net.electrisoma.visceralib.impl.dsp.v1.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientEntrypointNeoForge {

	public ClientEntrypointNeoForge(IEventBus modEventBus) {
		IEnvHelper.INSTANCE.runIfCurrent(IEnvHelper.EnvironmentEnum.CLIENT, ClientEntrypoint::init);
		modEventBus.addListener(this::onRegisterClientReloadListeners);
	}

	private void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new DSPPipelineReloadListener());
	}
}
