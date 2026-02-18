package net.electrisoma.visceralib.api.dsp.v1.data;

import net.minecraft.sounds.SoundSource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

public record DSPPipelineData(
		List<StageData> pipeline,
		List<String> sources
) {

	public record StageData(String type, Map<String, Float> parameters, float wetness) {}

	public static final Codec<StageData> STAGE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("type").forGetter(StageData::type),
			Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("parameters").forGetter(StageData::parameters),
			Codec.FLOAT.optionalFieldOf("wetness", 1.0f).forGetter(StageData::wetness)
	).apply(instance, StageData::new));

	public static final Codec<DSPPipelineData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			STAGE_CODEC.listOf().fieldOf("pipeline").forGetter(DSPPipelineData::pipeline),
			Codec.STRING.listOf().optionalFieldOf("sources", List.of()).forGetter(DSPPipelineData::sources)
	).apply(instance, DSPPipelineData::new));

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final List<StageData> stages = new ArrayList<>();
		private final Set<String> includedSources = new HashSet<>();
		private final Set<String> excludedSources = new HashSet<>();

		public StageBuilder stage(String type) {
			return new StageBuilder(this, type);
		}

		public Builder source(String... names) {
			this.includedSources.addAll(List.of(names));
			return this;
		}

		public Builder excludeSource(String... names) {
			this.excludedSources.addAll(List.of(names));
			return this;
		}

		public DSPPipelineData build() {
			List<String> finalSources = !excludedSources.isEmpty() ? Arrays.stream(SoundSource.values())
					.map(SoundSource::getName)
					.filter(name -> !excludedSources.contains(name))
					.toList() : List.copyOf(includedSources);

			return new DSPPipelineData(List.copyOf(stages), finalSources);
		}

		public static class StageBuilder {

			private final Builder parent;
			private final String type;
			private final Map<String, Float> parameters = new HashMap<>();
			private float wetness = 1.0f;

			StageBuilder(Builder parent, String type) {
				this.parent = parent;
				this.type = type;
			}

			public StageBuilder param(String key, float value) {
				parameters.put(key, value);
				return this;
			}

			public StageBuilder wetness(float wetness) {
				this.wetness = wetness;
				return this;
			}

			public Builder add() {
				parent.stages.add(new StageData(type, parameters, wetness));
				return parent;
			}
		}
	}
}
