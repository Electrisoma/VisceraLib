package net.electrisoma.visceralib.impl.registration.v1;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;

public class EntrypointFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Entrypoint.init();
    }
}
