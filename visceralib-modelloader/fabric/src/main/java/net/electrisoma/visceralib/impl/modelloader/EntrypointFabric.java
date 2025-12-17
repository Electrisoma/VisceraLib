package net.electrisoma.visceralib.impl.modelloader;

import net.fabricmc.api.ModInitializer;

public final class EntrypointFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Entrypoint.init();
    }
}
