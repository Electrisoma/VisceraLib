package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.platform.registration.v1.services.IDynamicRegistryHelper;

import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@AutoService(IDynamicRegistryHelper.class)
public class DynamicRegistryImpl implements IDynamicRegistryHelper {

	private record DataPackEntry<T>(ResourceKey<Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec) {}
	private final List<DataPackEntry<?>> pending = new ArrayList<>();

	@Override
	public <T> void registerDataPackRegistry(
			ResourceKey<Registry<T>> key,
			Codec<T> codec,
			@Nullable Codec<T> networkCodec
	) {
		pending.add(new DataPackEntry<>(key, codec, networkCodec));
	}

	@Override
	public void onNewDatapackRegistry(Object event) {
		if (event instanceof DataPackRegistryEvent.NewRegistry neoEvent) {
			pending.forEach(entry -> registerEntry(neoEvent, entry));
			pending.clear();
		}
	}

	private <T> void registerEntry(DataPackRegistryEvent.NewRegistry event, DataPackEntry<T> entry) {
		event.dataPackRegistry(
				entry.key(),
				entry.codec(),
				entry.networkCodec()
		);
	}
}
