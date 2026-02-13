package net.electrisoma.visceralib.api.registration.v1.registry.register.custom;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

public record VisceralRegistrySettings(
		boolean sync,
		@Nullable ResourceLocation defaultId
) {
	public static final VisceralRegistrySettings DEFAULT =
			new VisceralRegistrySettings(false, null);

	public static VisceralRegistrySettings synced() {
		return new VisceralRegistrySettings(true, null);
	}
}
