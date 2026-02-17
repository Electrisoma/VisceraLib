package net.electrisoma.visceralib.platform.core.services.event.server;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.core.server.VisceralServerLifecycleEvent;


public interface VisceraLibCoreServerEvents {

	VisceraLibCoreServerEvents INSTANCE = ServiceHelper.load(VisceraLibCoreServerEvents.class);

	void registerStarting(VisceralServerLifecycleEvent.Starting handler);
	void registerStarted(VisceralServerLifecycleEvent.Started handler);
	void registerStopping(VisceralServerLifecycleEvent.Stopping handler);
	void registerStopped(VisceralServerLifecycleEvent.Stopped handler);
}
