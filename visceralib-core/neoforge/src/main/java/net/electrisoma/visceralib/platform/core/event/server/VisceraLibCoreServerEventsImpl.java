package net.electrisoma.visceralib.platform.core.event.server;

import net.electrisoma.visceralib.event.core.server.VisceralServerLifecycleEvent;
import net.electrisoma.visceralib.platform.core.services.event.server.VisceraLibCoreServerEvents;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.*;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibCoreServerEvents.class)
public final class VisceraLibCoreServerEventsImpl implements VisceraLibCoreServerEvents {

	@Override
	public void registerStarting(VisceralServerLifecycleEvent.Starting h) {
		NeoForge.EVENT_BUS.addListener((ServerStartingEvent e) -> h.onLifecycle(e.getServer()));
	}

	@Override
	public void registerStarted(VisceralServerLifecycleEvent.Started h) {
		NeoForge.EVENT_BUS.addListener((ServerStartedEvent e) -> h.onLifecycle(e.getServer()));
	}

	@Override
	public void registerStopping(VisceralServerLifecycleEvent.Stopping h) {
		NeoForge.EVENT_BUS.addListener((ServerStoppingEvent e) -> h.onLifecycle(e.getServer()));
	}

	@Override
	public void registerStopped(VisceralServerLifecycleEvent.Stopped h) {
		NeoForge.EVENT_BUS.addListener((ServerStoppedEvent e) -> h.onLifecycle(e.getServer()));
	}
}
