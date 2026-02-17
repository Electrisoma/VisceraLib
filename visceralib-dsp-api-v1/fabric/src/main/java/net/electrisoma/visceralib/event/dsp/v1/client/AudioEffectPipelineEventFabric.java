package net.electrisoma.visceralib.event.dsp.v1.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public final class AudioEffectPipelineEventFabric {

	public static final Event<VisceralAudioEffectPipelineEvent.Hook> EVENT = EventFactory.createArrayBacked(
			VisceralAudioEffectPipelineEvent.Hook.class,
			listeners -> context ->
				Arrays.stream(listeners).forEach(listener -> listener.onAudioEffectPipeline(context))
	);

	private AudioEffectPipelineEventFabric() {}
}
