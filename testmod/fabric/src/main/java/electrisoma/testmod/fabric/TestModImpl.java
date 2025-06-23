package electrisoma.testmod.fabric;

import net.electrisoma.testmod.TestMod;

import net.electrisoma.visceralib.VisceraLib;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class TestModImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        TestMod.init();
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> VisceraLib.Fabric.CreativeTabFiller.register());
    }
}
