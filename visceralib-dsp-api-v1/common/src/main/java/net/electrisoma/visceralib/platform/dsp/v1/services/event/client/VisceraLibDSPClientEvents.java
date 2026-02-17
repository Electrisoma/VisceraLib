package net.electrisoma.visceralib.platform.dsp.v1.services.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.dsp.v1.client.VisceralAudioEffectPipelineEvent;

public interface VisceraLibDSPClientEvents {

	VisceraLibDSPClientEvents INSTANCE = ServiceHelper.load(VisceraLibDSPClientEvents.class);

	void registerPipelineProvider(VisceralAudioEffectPipelineEvent.Hook handler);
	void postPipelineEvent(VisceralAudioEffectPipelineEvent.Context context);
}
