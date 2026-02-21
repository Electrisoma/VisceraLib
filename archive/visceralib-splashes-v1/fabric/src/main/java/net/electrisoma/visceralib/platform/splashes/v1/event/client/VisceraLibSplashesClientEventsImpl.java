package net.electrisoma.visceralib.platform.splashes.v1.event.client;

import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEvent;
import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEventsFabric;
import net.electrisoma.visceralib.platform.splashes.v1.services.event.client.VisceraLibSplashesClientEvents;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibSplashesClientEvents.class)
public final class VisceraLibSplashesClientEventsImpl implements VisceraLibSplashesClientEvents {

	@Override
	public void registerSplashProviderPre(VisceralSplashEvent.Pre h) {
		VisceralSplashEventsFabric.PRE.register(h);
	}

	@Override
	public void registerSplashProviderPost(VisceralSplashEvent.Post h) {
		VisceralSplashEventsFabric.POST.register(h);
	}

	@Override
	public void postSplashPre(VisceralSplashEvent.Context context) {
		VisceralSplashEventsFabric.PRE.invoker().onSplashPre(context);
	}

	@Override
	public void postSplashPost(VisceralSplashEvent.Context context) {
		VisceralSplashEventsFabric.POST.invoker().onSplashPost(context);
	}
}
