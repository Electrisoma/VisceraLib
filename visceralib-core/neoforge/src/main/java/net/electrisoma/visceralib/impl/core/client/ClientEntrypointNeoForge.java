package net.electrisoma.visceralib.impl.core.client;

//import net.electrisoma.visceralib.api.core.config.VisceralConfigScreen;
import net.electrisoma.visceralib.impl.core.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientEntrypointNeoForge {

    public ClientEntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
//        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
//                () -> (container, parent) ->
//                        new VisceralConfigScreen(parent, Component.literal("VisceraLib Config")));
    }

    public static class ClientEvents {

//        public static void init(final FMLClientSetupEvent event) {}
    }
}
