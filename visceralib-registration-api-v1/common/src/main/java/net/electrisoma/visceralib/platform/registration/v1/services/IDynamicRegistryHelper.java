package net.electrisoma.visceralib.platform.registration.v1.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.Nullable;

public interface IDynamicRegistryHelper {

	IDynamicRegistryHelper INSTANCE = ServiceHelper.load(IDynamicRegistryHelper.class);

	<T> void registerDataPackRegistry(
			ResourceKey<Registry<T>> key,
			Codec<T> codec,
			@Nullable Codec<T> networkCodec
	);

	void onNewDatapackRegistry(Object event);
}
