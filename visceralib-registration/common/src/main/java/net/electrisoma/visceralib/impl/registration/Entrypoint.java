package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.impl.registration.test.TestBlockEntities;
import net.electrisoma.visceralib.impl.registration.test.TestBlocks;
import net.electrisoma.visceralib.impl.registration.test.TestItems;
import net.electrisoma.visceralib.impl.registration.test.TestTabs;

final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        testReg();
    }

    private static void testReg() {
        TestTabs.init();
        TestBlockEntities.init();
        TestBlocks.init();
        TestItems.init();
    }
}