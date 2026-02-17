package net.electrisoma.visceralib.api.dsp.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import com.mojang.serialization.JsonOps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO: fix `OpenAL Error [40965]: Pipeline failed for sound` error when multiple effects are loaded at once
// TODO: add affected sound sources as a field to resource pack
public class AudioFilterLoader extends SimpleJsonResourceReloadListener {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Map<ResourceLocation, AudioFilter> LOADED_FILTERS = new HashMap<>();
	private final static Logger LOG = LoggerFactory.getLogger("VisceraLib DSP/AudioFilterLoader");

	public AudioFilterLoader() {
		super(GSON, "audio_filters");
	}

	@Override
	protected void apply(
			Map<ResourceLocation, JsonElement> object,
			@NotNull ResourceManager rm,
			@NotNull ProfilerFiller profiler
	) {
		Map<ResourceLocation, AudioFilter> newMap = new HashMap<>();

		object.forEach((location, json) ->
				AudioFilter.DATA_CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(err -> {
					LOG.error("Failed to parse audio filter {}: {}", location, err);
				}).ifPresent(data -> {
					List<AudioFilter.StageDefinition> stages = data.pipeline().stream()
							.map(s -> {
								var res = AudioFilterManager.resolve(s.type());
								if (res == null) {
									LOG.warn("Unknown filter type '{}' in pipeline '{}'. Skipping stage.", s.type(), location);
									return Optional.<AudioFilter.StageDefinition>empty();
								}
								return Optional.of(new AudioFilter.StageDefinition(
										res.id(),
										s.parameters(),
										s.wetness(),
										res.isEffect()
								));
							})
							.flatMap(Optional::stream)
							.toList();

					if (!stages.isEmpty()) {
						newMap.put(location, new AudioFilter(location, stages));
					}
				})
		);

		LOADED_FILTERS.clear();
		LOADED_FILTERS.putAll(newMap);
		AudioFilterManager.reinitialize(newMap);
	}

	public static Map<ResourceLocation, AudioFilter> getFilters() {
		return LOADED_FILTERS;
	}
}
