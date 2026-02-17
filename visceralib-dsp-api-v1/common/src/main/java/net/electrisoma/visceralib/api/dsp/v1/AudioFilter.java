package net.electrisoma.visceralib.api.dsp.v1;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

public record AudioFilter(
		ResourceLocation id,
		List<StageDefinition> stages
) {
	public record StageDefinition(int alHandle, Map<String, Float> params, float defaultWetness, boolean isEffect) {}

	public record Data(List<StageData> pipeline) {}
	public record StageData(String type, Map<String, Float> parameters, float wetness) {}

	public static final Codec<AudioFilter.StageData> STAGE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("type").forGetter(AudioFilter.StageData::type),
			Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("parameters").forGetter(AudioFilter.StageData::parameters),
			Codec.FLOAT.optionalFieldOf("wetness", 1.0f).forGetter(AudioFilter.StageData::wetness)
	).apply(instance, AudioFilter.StageData::new));

	public static final Codec<AudioFilter.Data> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			AudioFilter.STAGE_CODEC.listOf().fieldOf("pipeline").forGetter(AudioFilter.Data::pipeline)
	).apply(instance, AudioFilter.Data::new));
}
