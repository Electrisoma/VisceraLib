package net.electrisoma.visceralib.event.registration.v1.common;

import net.electrisoma.visceralib.api.registration.v1.registry.custom.VisceralRegistrySettings;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.Nullable;

public final class RegistryRegistrationEvents {

	private RegistryRegistrationEvents() {}

	@FunctionalInterface
	public interface Static {

		void onRegister(StaticRegistrar registrar);
	}

	@FunctionalInterface
	public interface Dynamic {

		void onRegister(DynamicRegistrar registrar);
	}

	public interface StaticRegistrar {

		<T> Registry<T> register(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings);
	}

	public interface DynamicRegistrar {

		<T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec);
	}
}
