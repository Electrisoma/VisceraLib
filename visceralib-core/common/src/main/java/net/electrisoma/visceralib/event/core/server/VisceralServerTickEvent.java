package net.electrisoma.visceralib.event.core.server;

import net.minecraft.server.MinecraftServer;

public final class VisceralServerTickEvent {

	private VisceralServerTickEvent() {}

	@FunctionalInterface
	public interface Hook {

		void onServerTick(MinecraftServer server);
	}

	public interface Pre extends Hook {}

	public interface Post extends Hook {}
}
