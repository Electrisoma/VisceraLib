package net.electrisoma.visceralib.platform.core.event.server;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.event.core.server.VisceralServerLifecycleEvent;
import net.electrisoma.visceralib.event.core.server.VisceralServerTickEvent;
import net.electrisoma.visceralib.platform.core.services.event.server.VisceraLibCoreServerEvents;

import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibCoreServerEvents.class)
public final class VisceraLibCoreServerEventsImpl implements VisceraLibCoreServerEvents, IEventBusHelper {

	@Override
	public void registerStarting(VisceralServerLifecycleEvent.Starting h) {
		withNeoForgeBus(bus -> bus.addListener((ServerStartingEvent e) -> h.onLifecycle(e.getServer())));
	}

	@Override
	public void registerStarted(VisceralServerLifecycleEvent.Started h) {
		withNeoForgeBus(bus -> bus.addListener((ServerStartedEvent e) -> h.onLifecycle(e.getServer())));
	}

	@Override
	public void registerStopping(VisceralServerLifecycleEvent.Stopping h) {
		withNeoForgeBus(bus -> bus.addListener((ServerStoppingEvent e) -> h.onLifecycle(e.getServer())));
	}

	@Override
	public void registerStopped(VisceralServerLifecycleEvent.Stopped h) {
		withNeoForgeBus(bus -> bus.addListener((ServerStoppedEvent e) -> h.onLifecycle(e.getServer())));
	}

	@Override
	public void registerPreServerTick(VisceralServerTickEvent.Pre h) {
		withNeoForgeBus(bus -> bus.addListener((ServerTickEvent.Pre e) ->
				h.onServerTick(e.getServer())));
	}

	@Override
	public void registerPostServerTick(VisceralServerTickEvent.Post h) {
		withNeoForgeBus(bus -> bus.addListener((ServerTickEvent.Post e) ->
				h.onServerTick(e.getServer())));
	}
}
