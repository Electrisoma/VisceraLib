package net.electrisoma.resotech.mixin.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.electrisoma.resotech.api.registration.builders.FluidBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Shadow @Mutable private static float fogRed;
    @Shadow @Mutable private static float fogGreen;
    @Shadow @Mutable private static float fogBlue;

    // Redirect fluid type detection to avoid vanilla water fog type (prevents blue horizon)
    @Redirect(
            method = "setupColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;"
            )
    )
    private static FogType redirectGetFluidInCamera(Camera camera) {
        ClientLevel level = (ClientLevel) camera.getEntity().level();
        BlockPos pos = BlockPos.containing(camera.getPosition());
        FluidState fluidState = level.getFluidState(pos);

        if (!fluidState.isEmpty()) {
            Optional<FluidBuilder> builder = FluidBuilder.fromBlock(level.getBlockState(pos).getBlock());
            if (builder.isPresent()) {
                // Return NONE to avoid vanilla water fog effects on horizon
                return FogType.NONE;
            }
        }
        return camera.getFluidInCamera();
    }

    // Override fog color at the end of setupColor to forcibly set your fluid's color
    @Inject(method = "setupColor", at = @At("TAIL"))
    private static void overrideFogColor(
            Camera camera,
            float partialTicks,
            ClientLevel level,
            int renderDistanceChunks,
            float bossColorModifier,
            CallbackInfo ci
    ) {
        BlockPos pos = camera.getBlockPosition();
        FluidState fluidState = level.getFluidState(pos);

        if (!fluidState.isEmpty()) {
            Optional<FluidBuilder> builderOpt = FluidBuilder.fromBlock(level.getBlockState(pos).getBlock());
            if (builderOpt.isPresent()) {
                Vector3f customColor = new Vector3f(
                        (float) builderOpt.get().getFogColor().x,
                        (float) builderOpt.get().getFogColor().y,
                        (float) builderOpt.get().getFogColor().z
                );

                fogRed = customColor.x;
                fogGreen = customColor.y;
                fogBlue = customColor.z;

                RenderSystem.setShaderFogColor(fogRed, fogGreen, fogBlue);
            }
        }
    }

    // Override fog distance & shape to simulate smooth horizon fog fade with your custom fluid
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void overrideFogDistance(
            Camera camera,
            FogRenderer.FogMode fogMode,
            float farPlaneDistance,
            boolean shouldCreateFog,
            float partialTicks,
            CallbackInfo ci
    ) {
        ClientLevel level = (ClientLevel) camera.getEntity().level();
        BlockPos pos = camera.getBlockPosition();
        FluidState fluidState = level.getFluidState(pos);

        if (!fluidState.isEmpty()) {
            Optional<FluidBuilder> builderOpt = FluidBuilder.fromBlock(level.getBlockState(pos).getBlock());
            if (builderOpt.isPresent()) {
                float modifier = FluidBuilder.getFogDensity();
                float baseWaterFog = 96.0f;
                if (modifier != 1f) {
                    RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                    RenderSystem.setShaderFogStart(-8);
                    RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                }

                ci.cancel();
            }
        }
    }
}
