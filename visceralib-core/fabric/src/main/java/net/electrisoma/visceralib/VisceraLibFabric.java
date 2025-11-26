package net.electrisoma.visceralib;

import net.fabricmc.api.ModInitializer;

public class VisceraLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        VisceraLib.init();
    }
}
