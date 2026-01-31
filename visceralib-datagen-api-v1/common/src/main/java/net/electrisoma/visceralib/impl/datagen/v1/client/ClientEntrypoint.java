package net.electrisoma.visceralib.impl.datagen.v1.client;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.impl.datagen.v1.Constants;

public final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
    }
}
