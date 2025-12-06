package net.electrisoma.visceralib.impl.registration;

import net.fabricmc.api.ModInitializer;

public class EntrypointFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Entrypoint.init();
    }
}
