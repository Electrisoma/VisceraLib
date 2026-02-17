package net.electrisoma.visceralib.event.core.client;

import net.minecraft.client.Minecraft;

public final class VisceralClientTickEvent {

	private VisceralClientTickEvent() {}

	@FunctionalInterface
	public interface Hook {

		void onClientTick(Minecraft client);
	}

	public interface Pre extends Hook {}

	public interface Post extends Hook {}
}
