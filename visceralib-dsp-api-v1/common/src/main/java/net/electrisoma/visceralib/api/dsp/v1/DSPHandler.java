package net.electrisoma.visceralib.api.dsp.v1;

import net.electrisoma.visceralib.api.dsp.v1.data.DSPProcessor;
import net.electrisoma.visceralib.api.dsp.v1.openal.DSPRegistry;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;

import java.util.ArrayList;
import java.util.List;

public class DSPHandler {

	public record Pipeline(List<ActiveProcessor> processors) {}
	public record ActiveProcessor(DSPProcessor template, float wetness) {}

	public static Pipeline determinePipeline(SoundInstance sound) {
		Minecraft mc = Minecraft.getInstance();
		DSPContext context = new DSPContext(sound, mc.player, mc.level);
		VisceraLibDSPClientEvents.INSTANCE.postPipelineEvent(context);

		List<ActiveProcessor> active = new ArrayList<>();
		context.getAppliedChains().forEach(applied -> {
			DSPProcessor template = DSPRegistry.getTemplate(applied.id());
			if (template == null) return;

			if (!template.allowedSources().isEmpty() && !template.allowedSources().contains(sound.getSource()))
				return;

			active.add(new ActiveProcessor(template, applied.wetness()));
		});

		return new Pipeline(active);
	}
}
