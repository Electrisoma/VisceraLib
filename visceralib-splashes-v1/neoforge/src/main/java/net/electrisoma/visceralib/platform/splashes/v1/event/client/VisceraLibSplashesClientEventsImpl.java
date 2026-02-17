package net.electrisoma.visceralib.platform.splashes.v1.event.client;

import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEvent;
import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEventNeoForge;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.splashes.v1.services.event.client.VisceraLibSplashesClientEvents;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibSplashesClientEvents.class)
public final class VisceraLibSplashesClientEventsImpl implements VisceraLibSplashesClientEvents {

	IEventBus modBus = IPlatformHelper.INSTANCE.getModEventBus();

	@Override
	public void registerSplashProviderPre(VisceralSplashEvent.Pre h) {
		if (modBus != null) {
			modBus.addListener((VisceralSplashEventNeoForge.Pre event) -> h.onSplashPre(event));
		}
	}

	@Override
	public void registerSplashProviderPost(VisceralSplashEvent.Post h) {
		if (modBus != null) {
			modBus.addListener((VisceralSplashEventNeoForge.Post event) -> h.onSplashPost(event));
		}
	}

	@Override
	public void postSplashPre(VisceralSplashEvent.Context context) {
		postToBus(new VisceralSplashEventNeoForge.Pre(context));
	}

	@Override
	public void postSplashPost(VisceralSplashEvent.Context context) {
		postToBus(new VisceralSplashEventNeoForge.Post(context));
	}

	private void postToBus(Event event) {
		if (modBus != null) {
			modBus.post(event);
		}
	}
}
