package net.electrisoma.visceralib.api.datagen.v1.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import com.google.gson.JsonElement;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class VisceralDynamicRegistryProvider implements DataProvider {

	private final PackOutput output;
	private final String modid;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;
	private final RegistrySetBuilder builder = new RegistrySetBuilder();
	private final Map<ResourceKey<? extends Registry<?>>, Codec<?>> registries = new LinkedHashMap<>();

	public VisceralDynamicRegistryProvider(
			PackOutput output,
			String modid,
			CompletableFuture<HolderLookup.Provider> lookup
	) {
		this.output = output;
		this.modid = modid;
		this.lookupProvider = lookup;
	}

	protected <T> void add(
			ResourceKey<? extends Registry<T>> key,
			Codec<T> codec,
			RegistrySetBuilder.RegistryBootstrap<T> bootstrap
	) {
		this.registries.put(key, codec);
		this.builder.add(key, bootstrap);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput writer) {
		return this.lookupProvider.thenCompose(lookup -> {

			RegistryAccess.Frozen frozen = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
			HolderLookup.Provider fullLookup = this.builder.build(frozen);

			RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, fullLookup);
			List<CompletableFuture<?>> futures = new ArrayList<>();

			this.registries.forEach((key, codec) ->
					generateRegistry(futures, writer, fullLookup, ops, key, codec));

			return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
		});
	}

	@SuppressWarnings("unchecked")
	private <T> void generateRegistry(
			List<CompletableFuture<?>> futures,
			CachedOutput writer,
			HolderLookup.Provider lookup,
			RegistryOps<JsonElement> ops, ResourceKey<? extends Registry<?>> key,
			Codec<?> codec
	) {
		var registryKey = (ResourceKey<Registry<T>>) key;
		var typedCodec = (Codec<T>) codec;
		var registryLookup = lookup.lookupOrThrow(registryKey);
		var pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, registryKey.location().getPath());

		registryLookup.listElementIds()
				.filter(id -> id.location().getNamespace().equals(this.modid))
				.forEach(id -> {
					T value = registryLookup.getOrThrow(id).value();
					Path filePath = pathProvider.json(id.location());
					futures.add(encodeAndSave(writer, ops, typedCodec, value, filePath));
				});
	}

	private <T> CompletableFuture<?> encodeAndSave(
			CachedOutput writer,
			RegistryOps<JsonElement> ops,
			Codec<T> codec,
			T value,
			Path path
	) {
		return codec.encodeStart(ops, value).mapOrElse(
				json -> DataProvider.saveStable(writer, json, path),
				error -> {
					throw new IllegalStateException("Failed to encode " + path + ": " + error.message());
				}
		);
	}

	@Override
	public String getName() {
		return "Visceral Dynamic Registry Provider: " + modid;
	}
}
