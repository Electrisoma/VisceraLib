package net.electrisoma.visceralib.multiloader.neoforge;

import net.electrisoma.visceralib.api.neoforge.registration.VisceralBootstrapNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;

public class VisceralInitHookImpl {
    public static void bootstrap() {
        IEventBus eventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        VisceralBootstrapNeoForge.init(eventBus);
    }
}
