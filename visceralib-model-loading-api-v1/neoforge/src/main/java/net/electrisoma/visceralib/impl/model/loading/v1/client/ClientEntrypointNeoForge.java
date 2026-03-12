package net.electrisoma.visceralib.impl.model.loading.v1.client;

import net.electrisoma.visceralib.api.model.loading.v1.client.obj.VisceralObjLoader;
import net.electrisoma.visceralib.impl.model.loading.v1.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientEntrypointNeoForge {

	public ClientEntrypointNeoForge(IEventBus modEventBus) {
		IPlatformHelper.INSTANCE.registerModBus(modEventBus);
		IEnvHelper.INSTANCE.runIfCurrent(IEnvHelper.EnvironmentEnum.CLIENT, ClientEntrypoint::init);
		modEventBus.addListener(this::onRegisterReloadListeners);
	}

	public void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((ResourceManagerReloadListener) manager -> VisceralObjLoader.clearCache());
	}
}
