package net.electrisoma.visceralib.impl.registration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Constants.MOD_ID)
public class EntrypointNeoForge {

    public EntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(EntrypointNeoForge::init);
    }

    public static void init(final FMLCommonSetupEvent event) {
        Entrypoint.init();
    }
}
