package net.electrisoma.visceralib.platform.core.services.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.core.client.VisceralClientTickEvent;

public interface VisceraLibCoreClientEvents {

	VisceraLibCoreClientEvents INSTANCE = ServiceHelper.load(VisceraLibCoreClientEvents.class);

	void registerPreClientTick(VisceralClientTickEvent.Pre handler);
	void registerPostClientTick(VisceralClientTickEvent.Post handler);
}
