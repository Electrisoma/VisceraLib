package net.electrisoma.visceralib.event.splashes.v1.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public final class VisceralSplashEventsFabric {

	public static final Event<VisceralSplashEvent.Pre> PRE = EventFactory.createArrayBacked(
			VisceralSplashEvent.Pre.class,
			listeners -> (context) ->
					Arrays.stream(listeners).forEach(listener -> listener.onSplashPre(context))
	);

	public static final Event<VisceralSplashEvent.Post> POST = EventFactory.createArrayBacked(
			VisceralSplashEvent.Post.class,
			listeners -> (context) ->
				Arrays.stream(listeners).forEach(listener -> listener.onSplashPost(context))
	);

	private VisceralSplashEventsFabric() {}
}
