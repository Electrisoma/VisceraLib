package net.electrisoma.visceralib.api.dsp.v1;

import net.electrisoma.visceralib.api.dsp.v1.data.DSPPipelineData;
import net.electrisoma.visceralib.api.dsp.v1.data.DSPProcessor;
import net.electrisoma.visceralib.api.dsp.v1.openal.DSPManager;
import net.electrisoma.visceralib.api.dsp.v1.openal.DSPRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.ProfilerFiller;

import com.mojang.serialization.JsonOps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DSPPipelineReloadListener extends SimpleJsonResourceReloadListener {

	public final static String DIR = "audio_pipelines";
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final static Logger LOG = LoggerFactory.getLogger("VisceraLib/DSPPipelineReloadListener");

	public DSPPipelineReloadListener() {
		super(GSON, DIR);
	}

	@Override
	protected void apply(
			Map<ResourceLocation, JsonElement> object,
			@NotNull ResourceManager rm,
			@NotNull ProfilerFiller profiler
	) {
		Map<ResourceLocation, DSPProcessor> newMap = new HashMap<>();

		object.forEach((location, json) ->
				DSPPipelineData.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(LOG::error)
						.ifPresent(data -> {
							List<DSPProcessor.Stage> stages = data.pipeline().stream()
									.map(s -> {
										var res = DSPRegistry.resolveType(s.type());
										if (res != null) {
											return Optional.of(new DSPProcessor.Stage(
													res.id(),
													s.parameters(),
													s.wetness(),
													res.isEffect()
											));
										}
										LOG.warn("Unknown DSP stage type '{}' in {}", s.type(), location);
										return Optional.<DSPProcessor.Stage>empty();
									})
									.flatMap(Optional::stream)
									.toList();

							Set<SoundSource> sources = data.sources().stream()
									.map(jsonName -> {
										for (SoundSource source : SoundSource.values()) {
											if (source.getName().equalsIgnoreCase(jsonName))
												return source;
										}
										LOG.error("Invalid SoundSource '{}' defined in pipeline {}", jsonName, location);
										return null;
									})
									.filter(Objects::nonNull)
									.collect(Collectors.toSet());

							if (stages.isEmpty()) return;
							newMap.put(location, new DSPProcessor(location, stages, sources));
						})
		);

		DSPRegistry.updateTemplates(newMap);
		DSPManager.reinitialize();
	}
}
