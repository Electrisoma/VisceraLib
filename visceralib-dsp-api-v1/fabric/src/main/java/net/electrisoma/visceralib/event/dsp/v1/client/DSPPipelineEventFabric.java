package net.electrisoma.visceralib.event.dsp.v1.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public final class DSPPipelineEventFabric {

	public static final Event<DSPPipelineDefinition.Listener> EVENT = EventFactory.createArrayBacked(
			DSPPipelineDefinition.Listener.class,
			listeners -> context ->
				Arrays.stream(listeners).forEach(listener -> listener.onApply(context))
	);

	private DSPPipelineEventFabric() {}
}
