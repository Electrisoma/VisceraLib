package net.electrisoma.visceralib.impl.datagen;

import net.fabricmc.api.ModInitializer;

public final class EntrypointFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Entrypoint.init();
    }
}
