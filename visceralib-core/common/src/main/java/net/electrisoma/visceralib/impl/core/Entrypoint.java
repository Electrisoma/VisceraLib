package net.electrisoma.visceralib.impl.core;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.event.core.common.VisceralEvents;

public final class Entrypoint {

    public static void init() {
        EntrypointMessages.onCommon(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        VisceralEvents.registerAll();
    }
}