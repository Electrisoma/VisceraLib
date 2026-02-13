package net.electrisoma.visceralib.event.core.client;

import java.util.ServiceLoader;

public class VisceralClientEvents {

	private static final ServiceLoader<IVisceralClientListener> LISTENERS =
			ServiceLoader.load(IVisceralClientListener.class);

	public static void registerAll() {

		for (IVisceralClientListener listener : LISTENERS) {
			listener.register();
		}
	}
}
