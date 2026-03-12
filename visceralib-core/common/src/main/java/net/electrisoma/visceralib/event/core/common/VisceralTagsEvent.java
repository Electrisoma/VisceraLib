package net.electrisoma.visceralib.event.core.common;

import net.minecraft.core.RegistryAccess;

public final class VisceralTagsEvent {

	private VisceralTagsEvent() {}

	@FunctionalInterface
	public interface Hook {

		void onTagsLoaded(RegistryAccess registries, boolean client);
	}
}
