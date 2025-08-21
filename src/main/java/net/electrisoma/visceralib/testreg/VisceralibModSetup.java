package net.electrisoma.visceralib.testreg;

public class VisceralibModSetup {
    public static void register() {
        VisceralibTabs.init();
        VisceralibBlocks.init();
        VisceralibItems.init();
        //VisceralibFluids.init();
        VisceralibParticles.init();
    }
}
