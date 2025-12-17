package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;

final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
    }
}
