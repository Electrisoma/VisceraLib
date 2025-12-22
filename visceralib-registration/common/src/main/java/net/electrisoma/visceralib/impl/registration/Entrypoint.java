package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.api.registration.registry.TestRegistration;

public final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        TestRegistration.init();
    }

    private static void testReg() {
//        TestBlockEntities.init();
//        TestBlocks.init();
//        TestItems.init();
//        TestTabs.init();
    }
}