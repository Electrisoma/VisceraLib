package net.electrisoma.visceralib.registry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrar;

import static net.electrisoma.visceralib.VisceraLib.MOD_ID;

public class ModSetup {
    public static void register() {
        VisceralRegistrar registrar = VisceralRegistrar.create(MOD_ID);

        VisceraLibTabs.init();
        //VisceraLibFluids.init();
        VisceraLibBlocks.init();
        VisceraLibItems.init();
    }
}

