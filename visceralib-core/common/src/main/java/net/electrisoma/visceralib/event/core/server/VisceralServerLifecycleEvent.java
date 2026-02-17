package net.electrisoma.visceralib.event.core.server;

import net.minecraft.server.MinecraftServer;

public final class VisceralServerLifecycleEvent {

	private VisceralServerLifecycleEvent() {}

	@FunctionalInterface
	public interface Hook {

		void onLifecycle(MinecraftServer server);
	}

	public interface Starting extends Hook {}

	public interface Started  extends Hook {}

	public interface Stopping extends Hook {}

	public interface Stopped  extends Hook {}
}
