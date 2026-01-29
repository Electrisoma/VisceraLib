package net.electrisoma.visceralib.impl.registration.v1;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.impl.registration.v1.test.TestCreativeTabs;
import net.electrisoma.visceralib.impl.registration.v1.test.TestRegistry;

public final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        TestCreativeTabs.init();
        TestRegistry.init();
    }
}