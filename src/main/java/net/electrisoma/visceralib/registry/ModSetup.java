package net.electrisoma.visceralib.registry;

public class ModSetup {
    public static void register() {
        VisceraLibTabs.init();
        VisceraLibFluids.init();
        VisceraLibBlocks.init();
        VisceraLibItems.init();
    }
}

