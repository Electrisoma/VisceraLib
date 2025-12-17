package net.electrisoma.visceralib.impl.datagen;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;

final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
    }
}
