package net.electrisoma.visceralib.impl.item.v1.client;

import net.electrisoma.visceralib.api.item.v1.client.ext.VisceralClientExtensionsManager;
import net.electrisoma.visceralib.impl.item.v1.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientEntrypointNeoForge {

	public ClientEntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
		IEnvHelper.INSTANCE.runIfCurrent(IEnvHelper.EnvironmentEnum.CLIENT, ClientEntrypoint::init);
		modEventBus.addListener(this::onClientSetup);
	}

	private void onClientSetup(final FMLClientSetupEvent event) {
		VisceralClientExtensionsManager.init();
	}
}
