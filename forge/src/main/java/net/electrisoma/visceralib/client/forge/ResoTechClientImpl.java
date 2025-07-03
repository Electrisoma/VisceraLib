package net.electrisoma.visceralib.client.forge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.client.VisceralLibClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = VisceraLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResoTechClientImpl {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        VisceralLibClient.init();
        registerClientEvents();
    }

    private static void registerClientEvents() {
        MinecraftForge.EVENT_BUS.register(ClientEventsImpl.class);
    }

//    @SubscribeEvent
//    private static void initializeClient(IClientFluidTypeExtensions event) {
//        FluidBuilder.getAllAttributes().forEach((attributes ->
//                event.registerFluidType(new IClientFluidTypeExtensions() {
//            @Override public @NotNull ResourceLocation getStillTexture() {
//                return attributes.getSourceTexture();
//            }
//            @Override public @NotNull ResourceLocation getFlowingTexture() {
//                return attributes.getFlowingTexture();
//            }
//        }, attributes.getFlowingFluid().getFluidType())));
//    }
}
