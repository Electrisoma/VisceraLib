package net.electrisoma.visceralib.event.registration.v1.common;

import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface StaticRegistryRegistrar {

	<T> Registry<T> register(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings);
}
