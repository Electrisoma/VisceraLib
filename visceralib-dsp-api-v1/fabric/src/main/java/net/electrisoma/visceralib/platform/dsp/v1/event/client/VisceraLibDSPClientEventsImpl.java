package net.electrisoma.visceralib.platform.dsp.v1.event.client;

import net.electrisoma.visceralib.event.dsp.v1.client.AudioEffectPipelineEventFabric;
import net.electrisoma.visceralib.event.dsp.v1.client.VisceralAudioEffectPipelineEvent;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibDSPClientEvents.class)
public final class VisceraLibDSPClientEventsImpl implements VisceraLibDSPClientEvents {

	@Override
	public void registerPipelineProvider(VisceralAudioEffectPipelineEvent.Hook h) {
		AudioEffectPipelineEventFabric.EVENT.register(h);
	}

	@Override
	public void postPipelineEvent(VisceralAudioEffectPipelineEvent.Context context) {
		AudioEffectPipelineEventFabric.EVENT.invoker().onAudioEffectPipeline(context);
	}
}
