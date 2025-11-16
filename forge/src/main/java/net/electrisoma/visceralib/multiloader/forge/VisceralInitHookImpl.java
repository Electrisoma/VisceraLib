package net.electrisoma.visceralib.multiloader.forge;


import net.electrisoma.visceralib.api.forge.registration.VisceralBootstrapForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class VisceralInitHookImpl {
    public static void bootstrap() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        VisceralBootstrapForge.init(eventBus);
    }
}
