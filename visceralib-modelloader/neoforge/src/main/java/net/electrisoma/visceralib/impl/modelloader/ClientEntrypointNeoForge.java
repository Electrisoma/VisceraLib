package net.electrisoma.visceralib.impl.modelloader;

import net.electrisoma.visceralib.impl.modelloader.client.ClientEntrypoint;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientEntrypointNeoForge {

    public ClientEntrypointNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(ClientEntrypointNeoForge::init);
    }

    public static void init(final FMLClientSetupEvent event) {
        IEnvHelper.INSTANCE.runIfCurrent(IEnvHelper.EnvironmentEnum.CLIENT, ClientEntrypoint::init);
    }

//    @SubscribeEvent
//    private static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
//    }
}
