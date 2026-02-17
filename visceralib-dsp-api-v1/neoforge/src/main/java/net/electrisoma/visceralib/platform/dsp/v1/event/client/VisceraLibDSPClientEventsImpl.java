package net.electrisoma.visceralib.platform.dsp.v1.event.client;

import net.electrisoma.visceralib.event.dsp.v1.client.AudioEffectPipelineEventNeoForge;
import net.electrisoma.visceralib.event.dsp.v1.client.VisceralAudioEffectPipelineEvent;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import net.neoforged.neoforge.common.NeoForge;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibDSPClientEvents.class)
public final class VisceraLibDSPClientEventsImpl implements VisceraLibDSPClientEvents {

	@Override
	public void registerPipelineProvider(VisceralAudioEffectPipelineEvent.Hook h) {
		NeoForge.EVENT_BUS.addListener((AudioEffectPipelineEventNeoForge event) -> h.onAudioEffectPipeline(event));
	}

	@Override
	public void postPipelineEvent(VisceralAudioEffectPipelineEvent.Context context) {
		NeoForge.EVENT_BUS.post(new AudioEffectPipelineEventNeoForge(context));
	}
}
