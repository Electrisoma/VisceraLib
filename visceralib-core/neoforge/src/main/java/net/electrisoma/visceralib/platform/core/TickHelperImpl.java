package net.electrisoma.visceralib.platform.core;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.ITickHelper;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@AutoService(ITickHelper.class)
public class TickHelperImpl implements ITickHelper {

    @Override
    public void registerTickListener(Runnable tickTask) {
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) ->
            tickTask.run()
        );
    }
}
