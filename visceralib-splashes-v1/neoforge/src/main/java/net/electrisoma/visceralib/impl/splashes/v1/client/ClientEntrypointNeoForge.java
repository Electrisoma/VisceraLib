package net.electrisoma.visceralib.impl.splashes.v1.client;

import net.electrisoma.visceralib.impl.splashes.v1.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientEntrypointNeoForge {

	public ClientEntrypointNeoForge(IEventBus modEventBus) {
		IEnvHelper.INSTANCE.runIfCurrent(IEnvHelper.EnvironmentEnum.CLIENT, ClientEntrypoint::init);
	}
}
