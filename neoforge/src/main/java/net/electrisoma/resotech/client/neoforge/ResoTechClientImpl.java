package net.electrisoma.resotech.client.neoforge;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.client.ResoTechClient;
import net.electrisoma.resotech.registry.ResoTechFluids;

import net.minecraft.resources.ResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = ResoTech.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ResoTechClientImpl {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ResoTechClient.init();
    }

    @SubscribeEvent
    private static void initializeClient(RegisterClientExtensionsEvent event) {
        ResoTechFluids.FLUIDS_INFOS.forEach((attributes ->
                event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return attributes.getSourceTexture();
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return attributes.getFlowingTexture();
            }
        }, attributes.getFlowingFluid().getFluidType())));
    }

}
