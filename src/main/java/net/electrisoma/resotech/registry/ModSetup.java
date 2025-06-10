package net.electrisoma.resotech.registry;

public class ModSetup {
    public static void register() {
        ResoTechTabs.init();
        ResoTechFluids.init();
        ResoTechBlocks.init();
        ResoTechItems.init();
    }
}

