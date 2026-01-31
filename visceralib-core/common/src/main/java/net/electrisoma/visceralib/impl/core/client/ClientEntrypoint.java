package net.electrisoma.visceralib.impl.core.client;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.event.core.client.ClientTickEvents;
import net.electrisoma.visceralib.event.core.client.VisceralClientEvents;
import net.electrisoma.visceralib.impl.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        VisceralClientEvents.registerAll();
    }
}