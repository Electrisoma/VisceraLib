package net.electrisoma.visceralib.event.registration.v1.common;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface DynamicRegistryRegistrar {

	<T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec);
}
