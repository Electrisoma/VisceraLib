package net.electrisoma.testmod.registry;

public class ModSetup {
    public static void register() {
        TestModTabs.init();
        TestModFluids.init();
        TestModBlocks.init();
        TestModItems.init();
    }
}

