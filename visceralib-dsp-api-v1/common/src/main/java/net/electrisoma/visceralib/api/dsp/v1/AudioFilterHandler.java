package net.electrisoma.visceralib.api.dsp.v1;

import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;

import java.util.ArrayList;
import java.util.List;

public class AudioFilterHandler {

	public record PipelineResult(List<ActiveChain> chains) {
		public record ActiveChain(AudioFilter template, float wetness, boolean global) {}
	}

	public static PipelineResult determineConfiguration(SoundInstance sound) {
		Minecraft mc = Minecraft.getInstance();
		AudioFilterContext context = new AudioFilterContext(sound, mc.player, mc.level);
		VisceraLibDSPClientEvents.INSTANCE.postPipelineEvent(context);

		List<PipelineResult.ActiveChain> activeChains = new ArrayList<>();

		for (var applied : context.getAppliedChains()) {
			AudioFilter template = AudioFilterManager.getTemplate(applied.id());
			if (template != null) {
				activeChains.add(new PipelineResult.ActiveChain(template, applied.wetness(), applied.global()));
			}
		}

		return new PipelineResult(activeChains);
	}
}
