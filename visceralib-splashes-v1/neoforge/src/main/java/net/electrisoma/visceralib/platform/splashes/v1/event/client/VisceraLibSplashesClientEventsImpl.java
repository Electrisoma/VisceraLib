package net.electrisoma.visceralib.platform.splashes.v1.event.client;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEvent;
import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEventNeoForge;
import net.electrisoma.visceralib.platform.splashes.v1.services.event.client.VisceraLibSplashesClientEvents;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibSplashesClientEvents.class)
public final class VisceraLibSplashesClientEventsImpl implements VisceraLibSplashesClientEvents, IEventBusHelper {

	@Override
	public void registerSplashProviderPre(VisceralSplashEvent.Pre h) {
		withModBus(bus -> bus.addListener((VisceralSplashEventNeoForge.Pre event) -> h.onSplashPre(event)));
	}

	@Override
	public void registerSplashProviderPost(VisceralSplashEvent.Post h) {
		withModBus(bus -> bus.addListener((VisceralSplashEventNeoForge.Post event) -> h.onSplashPost(event)));
	}

	@Override
	public void postSplashPre(VisceralSplashEvent.Context context) {
		withModBus(bus -> bus.post(new VisceralSplashEventNeoForge.Pre(context)));
	}

	@Override
	public void postSplashPost(VisceralSplashEvent.Context context) {
		withModBus(bus -> bus.post(new VisceralSplashEventNeoForge.Post(context)));
	}
}
