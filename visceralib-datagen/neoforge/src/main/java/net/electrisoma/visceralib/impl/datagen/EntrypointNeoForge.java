package net.electrisoma.visceralib.impl.datagen;

import net.electrisoma.visceralib.impl.datagen.test.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class EntrypointNeoForge {

    public EntrypointNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Entrypoint.init();
        ModItems.register(modEventBus);
        modEventBus.addListener(DataGeneratorNeoForge::gatherData);
    }
}
