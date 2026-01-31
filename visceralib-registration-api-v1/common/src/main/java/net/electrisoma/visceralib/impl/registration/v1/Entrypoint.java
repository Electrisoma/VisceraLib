package net.electrisoma.visceralib.impl.registration.v1;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.impl.registration.v1.test.TestSetup;

public final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        TestSetup.init();
    }
}