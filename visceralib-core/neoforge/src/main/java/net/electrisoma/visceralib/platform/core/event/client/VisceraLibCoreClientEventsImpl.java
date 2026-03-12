package net.electrisoma.visceralib.platform.core.event.client;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.event.core.client.VisceralClientTickEvent;
import net.electrisoma.visceralib.platform.core.services.event.client.VisceraLibCoreClientEvents;

import net.neoforged.neoforge.client.event.ClientTickEvent;

import net.minecraft.client.Minecraft;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibCoreClientEvents.class)
public final class VisceraLibCoreClientEventsImpl implements VisceraLibCoreClientEvents, IEventBusHelper {

	@Override
	public void registerPreClientTick(VisceralClientTickEvent.Pre h) {
		withNeoForgeBus(bus -> bus.addListener((ClientTickEvent.Pre event) ->
				h.onClientTick(Minecraft.getInstance())));
	}

	@Override
	public void registerPostClientTick(VisceralClientTickEvent.Post h) {
		withNeoForgeBus(bus -> bus.addListener((ClientTickEvent.Post event) ->
				h.onClientTick(Minecraft.getInstance())));
	}
}
