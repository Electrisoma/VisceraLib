package net.electrisoma.visceralib.platform.core;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.ITickHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@AutoService(ITickHelper.class)
public class TickHelperImpl implements ITickHelper {

    @Override
    public void registerTickListener(Runnable tickTask) {
        ClientTickEvents.END_CLIENT_TICK.register(client ->
            tickTask.run()
        );
    }
}