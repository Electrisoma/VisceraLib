package net.electrisoma.visceralib.platform.splashes.v1.services.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEvent;



public interface VisceraLibSplashesClientEvents {

	VisceraLibSplashesClientEvents INSTANCE = ServiceHelper.load(VisceraLibSplashesClientEvents.class);

	void registerSplashProviderPre(VisceralSplashEvent.Pre handler);
	void registerSplashProviderPost(VisceralSplashEvent.Post handler);

	void postSplashPre(VisceralSplashEvent.Context context);
	void postSplashPost(VisceralSplashEvent.Context context);
}
