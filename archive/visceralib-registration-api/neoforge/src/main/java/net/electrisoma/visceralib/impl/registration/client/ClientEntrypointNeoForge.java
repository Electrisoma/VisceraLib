package net.electrisoma.visceralib.impl.registration.client;

import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class ClientEntrypointNeoForge {

    public ClientEntrypointNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(ClientEntrypointNeoForge::init);
    }

    public static void init(final FMLClientSetupEvent event) {
        IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
    }
}