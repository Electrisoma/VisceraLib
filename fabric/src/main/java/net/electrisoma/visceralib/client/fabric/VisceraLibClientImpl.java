package net.electrisoma.visceralib.client.fabric;

import net.electrisoma.visceralib.client.ResoTechClient;
import net.fabricmc.api.ClientModInitializer;

public class VisceraLibClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResoTechClient.init();
    }
}
