package net.electrisoma.visceralib.client.neoforge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.client.VisceralLibClient;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = VisceraLib.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class VisceralLibClientImpl {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        VisceralLibClient.init();
    }
}
