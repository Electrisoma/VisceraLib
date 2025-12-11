package net.electrisoma.visceralib.impl.modelloader;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class EntrypointNeoForge {

    private EntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Entrypoint.init();
    }
}
