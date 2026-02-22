package net.electrisoma.visceralib.impl.registration.v1.client;

import net.electrisoma.visceralib.impl.registration.v1.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class ClientEntrypointNeoForge {

	public ClientEntrypointNeoForge(IEventBus modEventBus) {
		IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
	}
}
