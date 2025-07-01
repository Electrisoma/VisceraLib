package net.electrisoma.visceralib.client.neoforge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.builders.FluidBuilder;
import net.electrisoma.visceralib.client.ResoTechClient;

import net.minecraft.resources.ResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = VisceraLib.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ResoTechClientImpl {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ResoTechClient.init();
        registerClientEvents();
    }

    private static void registerClientEvents() {
        NeoForge.EVENT_BUS.register(ClientEventsImpl.class);
    }

    @SubscribeEvent
    private static void initializeClient(RegisterClientExtensionsEvent event) {
//        FluidBuilder.getAllAttributes().forEach((attributes ->
//                event.registerFluidType(new IClientFluidTypeExtensions() {
//            @Override public @NotNull ResourceLocation getStillTexture() {
//                return attributes.getSourceTexture();
//            }
//            @Override public @NotNull ResourceLocation getFlowingTexture() {
//                return attributes.getFlowingTexture();
//            }
//        }, attributes.getFlowingFluid().getFluidType())));
    }
}
