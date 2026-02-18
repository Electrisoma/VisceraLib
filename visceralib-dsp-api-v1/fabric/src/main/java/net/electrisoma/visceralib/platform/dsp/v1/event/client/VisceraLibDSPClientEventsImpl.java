package net.electrisoma.visceralib.platform.dsp.v1.event.client;

import net.electrisoma.visceralib.event.dsp.v1.client.DSPPipelineDefinition;
import net.electrisoma.visceralib.event.dsp.v1.client.DSPPipelineEventFabric;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibDSPClientEvents.class)
public final class VisceraLibDSPClientEventsImpl implements VisceraLibDSPClientEvents {

	@Override
	public void registerPipelineProvider(DSPPipelineDefinition.Listener h) {
		DSPPipelineEventFabric.EVENT.register(h);
	}

	@Override
	public void postPipelineEvent(DSPPipelineDefinition.Context context) {
		DSPPipelineEventFabric.EVENT.invoker().onApply(context);
	}
}
