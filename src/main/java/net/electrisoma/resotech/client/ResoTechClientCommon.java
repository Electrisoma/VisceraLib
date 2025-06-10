package net.electrisoma.resotech.client;

import net.electrisoma.resotech.registry.ResoTechFluids;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class ResoTechClientCommon {

    public static Fluid getCameraFluid(Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) return null;

        BlockPos blockPos = camera.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);

        if (camera.getPosition().y >= blockPos.getY() + fluidState.getHeight(level, blockPos)) {
            return null;
        }

        return fluidState.getType();
    }

    public static void setFogColor(Camera camera, SetColorWrapper wrapper) {
        Fluid fluid = getCameraFluid(camera);
        if (!isResoTechFluid(fluid)) return;

        if (ResoTechFluids.TEST_FLUID.get().isSame(fluid)) {
            wrapper.setFogColor(0.0f, 0.5f, 0.5f);
            return;
        }
    }

    public interface SetColorWrapper {
        void setFogColor(float r, float g, float b);
    }

    private static float previousDensity = -1f;

    public static float getFogDensity(Camera camera, float farPlane) {
        Fluid fluid = getCameraFluid(camera);
        if (fluid == null || !isResoTechFluid(fluid)) {
            previousDensity = -1f;
            return -1f;
        }

        float targetDensity = 1f / 16f;
        float smoothDensity = smoothFog(previousDensity, targetDensity, 0.15f);
        previousDensity = smoothDensity;
        return smoothDensity;
    }

    private static float smoothFog(float from, float to, float alpha) {
        if (from < 0) return to;
        return from + (to - from) * alpha;
    }

    public static boolean isResoTechFluid(Fluid fluid) {
        return fluid != null && ResoTechFluids.TEST_FLUID.get().isSame(fluid);
    }
}
