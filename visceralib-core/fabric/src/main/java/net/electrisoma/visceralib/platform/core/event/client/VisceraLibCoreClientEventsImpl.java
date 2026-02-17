package net.electrisoma.visceralib.platform.core.event.client;

import net.electrisoma.visceralib.event.core.client.VisceralClientTickEvent;
import net.electrisoma.visceralib.platform.core.services.event.client.VisceraLibCoreClientEvents;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibCoreClientEvents.class)
public final class VisceraLibCoreClientEventsImpl implements VisceraLibCoreClientEvents {

	@Override
	public void registerPreClientTick(VisceralClientTickEvent.Pre h) {
		ClientTickEvents.START_CLIENT_TICK.register(h::onClientTick);
	}

	@Override
	public void registerPostClientTick(VisceralClientTickEvent.Post h) {
		ClientTickEvents.END_CLIENT_TICK.register(h::onClientTick);
	}
}
