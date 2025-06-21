package net.electrisoma.resotech.client.fabric;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.electrisoma.resotech.api.registration.fluid.ResoLiquidBlock;
import net.electrisoma.resotech.client.ResoTechClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FogType;

public class ResoTechClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResoTechClient.init();
    }
}
