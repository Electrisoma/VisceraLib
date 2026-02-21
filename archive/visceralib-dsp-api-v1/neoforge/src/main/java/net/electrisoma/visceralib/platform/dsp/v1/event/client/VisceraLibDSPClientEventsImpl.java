package net.electrisoma.visceralib.platform.dsp.v1.event.client;

import net.electrisoma.visceralib.event.dsp.v1.client.DSPPipelineDefinition;
import net.electrisoma.visceralib.event.dsp.v1.client.DSPPipelineEventNeoForge;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import net.neoforged.neoforge.common.NeoForge;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibDSPClientEvents.class)
public final class VisceraLibDSPClientEventsImpl implements VisceraLibDSPClientEvents {

	@Override
	public void registerPipelineProvider(DSPPipelineDefinition.Listener h) {
		NeoForge.EVENT_BUS.addListener((DSPPipelineEventNeoForge event) -> h.onApply(event));
	}

	@Override
	public void postPipelineEvent(DSPPipelineDefinition.Context context) {
		NeoForge.EVENT_BUS.post(new DSPPipelineEventNeoForge(context));
	}
}
