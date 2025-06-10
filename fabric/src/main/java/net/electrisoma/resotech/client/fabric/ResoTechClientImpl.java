package net.electrisoma.resotech.client.fabric;

import net.electrisoma.resotech.client.ResoTechClient;
import net.fabricmc.api.ClientModInitializer;

public class ResoTechClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResoTechClient.init();
    }
}