package net.electrisoma.resotech.client.fabric;

import net.createmod.catnip.platform.FabricFluidHelper;
import net.electrisoma.resotech.client.ResoTechClient;
import net.electrisoma.resotech.client.ResoTechClientCommon;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;

public class ResoTechClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResoTechClient.init();
    }
}