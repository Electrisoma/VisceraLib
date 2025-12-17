package net.electrisoma.visceralib.impl.datagen;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;

final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
    }
}