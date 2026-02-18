package net.electrisoma.visceralib.api.dsp.v1.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record DSPProcessor(
		ResourceLocation id,
		List<Stage> stages,
		Set<SoundSource> allowedSources
) {

	public record Stage(int alHandle, Map<String, Float> params, float defaultWetness, boolean isEffect) {}
}
