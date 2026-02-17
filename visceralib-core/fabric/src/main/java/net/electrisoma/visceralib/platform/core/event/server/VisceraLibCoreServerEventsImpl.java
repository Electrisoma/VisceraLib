package net.electrisoma.visceralib.platform.core.event.server;

import net.electrisoma.visceralib.event.core.server.VisceralServerLifecycleEvent;
import net.electrisoma.visceralib.platform.core.services.event.server.VisceraLibCoreServerEvents;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibCoreServerEvents.class)
public final class VisceraLibCoreServerEventsImpl implements VisceraLibCoreServerEvents {

	@Override
	public void registerStarting(VisceralServerLifecycleEvent.Starting h) {
		ServerLifecycleEvents.SERVER_STARTING.register(h::onLifecycle);
	}

	@Override
	public void registerStarted(VisceralServerLifecycleEvent.Started h) {
		ServerLifecycleEvents.SERVER_STARTED.register(h::onLifecycle);
	}

	@Override
	public void registerStopping(VisceralServerLifecycleEvent.Stopping h) {
		ServerLifecycleEvents.SERVER_STOPPING.register(h::onLifecycle);
	}

	@Override
	public void registerStopped(VisceralServerLifecycleEvent.Stopped h) {
		ServerLifecycleEvents.SERVER_STOPPED.register(h::onLifecycle);
	}
}
