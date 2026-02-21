package net.electrisoma.visceralib.api.dsp.v1.data;

import net.electrisoma.visceralib.api.dsp.v1.DSPPipelineReloadListener;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.JsonOps;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class DSPPipelineProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;
	private final Map<ResourceLocation, DSPPipelineData> entries = new HashMap<>();

	public DSPPipelineProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, DSPPipelineReloadListener.DIR);
	}

	protected abstract void generatePipelines(BiConsumer<ResourceLocation, DSPPipelineData> consumer);

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
		generatePipelines(entries::put);

		List<CompletableFuture<?>> futures = new ArrayList<>();
		entries.forEach((location, data) -> {
			Path path = pathProvider.json(location);
			JsonElement json = DSPPipelineData.CODEC.encodeStart(JsonOps.INSTANCE, data)
					.getOrThrow(msg ->
							new IllegalStateException("Failed to encode DSP pipeline " + location + ": " + msg));

			futures.add(DataProvider.saveStable(cache, json, path));
		});

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public @NotNull String getName() {
		return "DSP Pipelines";
	}
}
