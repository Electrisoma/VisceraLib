package net.electrisoma.visceralib.impl.core;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
//import net.electrisoma.visceralib.event.core.VisceralEventBus;

final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
//        VisceralEventBus.initClient();
    }
}