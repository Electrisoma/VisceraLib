package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;

public final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        TestRegistry.init();
    }
}