package net.electrisoma.visceralib.client.neoforge;

import com.mojang.blaze3d.shaders.FogShape;
import net.electrisoma.visceralib.client.tooltips.TooltipModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientEventsImpl {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) return;
        TooltipModifier modifier = TooltipModifier.get(event.getItemStack().getItem());
        modifier.modify(event.getItemStack(), event.getToolTip());
    }

//    @SubscribeEvent
//    public static void onRenderFog(ViewportEvent.RenderFog event) {
//        Level level = Minecraft.getInstance().level;
//        if (level == null) return;
//
//        Vec3 eyePos = event.getCamera().getPosition();
//        if (isCameraInCustomFluid(level, eyePos)) {
//            event.setCanceled(true);
//
//            float start = -8;
//            float end = FluidBuilder.getFogDensity();
//            float baseWaterFog = 96.0f;
//
//            event.setCanceled(true);
//            event.setNearPlaneDistance(start);
//            event.setFarPlaneDistance(baseWaterFog/end);
//            event.setFogShape(FogShape.CYLINDER);
//        }
//    }
//
//    @SubscribeEvent
//    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
//        Level level = Minecraft.getInstance().level;
//        if (level == null) return;
//
//        Vec3 eyePos = event.getCamera().getPosition();
//        BlockPos pos = BlockPos.containing(eyePos);
//        FluidState fluidState = level.getFluidState(pos);
//
//        if (fluidState.isEmpty()) return;
//
//        Fluid fluid = fluidState.getType();
//        double fluidSurfaceY = pos.getY() + fluidState.getHeight(level, pos);
//
//        if (eyePos.y < fluidSurfaceY) {
//            FluidBuilder.getAllBuilders().stream()
//                    .filter(builder -> builder.getStill().get() == fluid || builder.getFlowing().get() == fluid)
//                    .findFirst()
//                    .ifPresent(builder -> {
//                        var fogColor = builder.getFogColor();
//                        event.setRed((float) fogColor.x);
//                        event.setGreen((float) fogColor.y);
//                        event.setBlue((float) fogColor.z);
//                    });
//        }
//    }
//
//    private static boolean isCameraInCustomFluid(Level level, Vec3 eyePos) {
//        BlockPos eyeBlockPos = BlockPos.containing(eyePos);
//        FluidState fluidState = level.getFluidState(eyeBlockPos);
//
//        if (fluidState.isEmpty()) return false;
//
//        Fluid fluid = fluidState.getType();
//        double fluidSurfaceY = eyeBlockPos.getY() + fluidState.getHeight(level, eyeBlockPos);
//
//        if (eyePos.y < fluidSurfaceY) {
//            return FluidBuilder.getAllBuilders().stream()
//                    .anyMatch(builder -> builder.getStill().get() == fluid || builder.getFlowing().get() == fluid);
//        }
//
//        return false;
//    }
}
