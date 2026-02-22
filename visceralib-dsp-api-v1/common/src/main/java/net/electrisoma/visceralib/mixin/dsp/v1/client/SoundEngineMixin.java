package net.electrisoma.visceralib.mixin.dsp.v1.client;

import net.electrisoma.visceralib.api.dsp.v1.DSPHandler;
import net.electrisoma.visceralib.api.dsp.v1.data.DSPProcessor;
import net.electrisoma.visceralib.api.dsp.v1.openal.DSPManager;
import net.electrisoma.visceralib.api.dsp.v1.openal.DSPRegistry;
import net.electrisoma.visceralib.api.dsp.v1.openal.DSPResource;
import net.electrisoma.visceralib.mixin.dsp.v1.client.accessor.ChannelAccessor;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;

import com.mojang.blaze3d.audio.Channel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {

	@Unique
	private final static Logger LOG = LoggerFactory.getLogger("VisceraLib/SoundEngineMixin");

	@Inject(
			method = "loadLibrary",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Listener;reset()V")
	)
	private void initAudioFilters(CallbackInfo ci) {
		DSPManager.reinitialize();
	}

	@Inject(
			method = { "destroy", "emergencyShutdown" },
			at = @At("HEAD")
	)
	private void disposeAudioFilters(CallbackInfo ci) {
		DSPManager.dispose();
	}

	@WrapOperation(
			method = "play",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V", ordinal = 0)
	)
	private void applyFilterOnPlay(
			ChannelAccess.ChannelHandle channelHandle,
			Consumer<Channel> consumer,
			Operation<Void> original,
			SoundInstance sound
	) {
		if (sound != null) viscera$applyDSP(channelHandle, sound);
		original.call(channelHandle, consumer);
	}

	@WrapOperation(
			method = "tickNonPaused",
			at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;", remap = false)
	)
	private Object captureAndApplyFilterDuringTick(
			Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry,
			Operation<Object> original
	) {
		ChannelAccess.ChannelHandle channelHandle = (ChannelAccess.ChannelHandle) original.call(entry);
		if (entry.getKey() != null && channelHandle != null)
			viscera$applyDSP(channelHandle, entry.getKey());
		return channelHandle;
	}

	@Unique
	private void viscera$applyDSP(
			ChannelAccess.ChannelHandle handle,
			SoundInstance sound
	) {
		if (!DSPManager.isAvailable()) return;

		DSPHandler.Pipeline pipeline = DSPHandler.determinePipeline(sound);

		handle.execute(channel -> {
			int src = ((ChannelAccessor) channel).getSourceId();
			if (src <= 0) return;

			if (pipeline.processors().isEmpty()) {
				DSPManager.cleanSource(src);
				return;
			}

			boolean tracked = DSPManager.isTracked(src);

			if (!tracked) DSPManager.cleanSource(src);

			int auxIdx = 0;
			for (var proc : pipeline.processors()) {
				for (var stage : proc.template().stages()) {
					float wet = proc.wetness() * stage.defaultWetness();

					if (wet <= 0.001f) continue;

					if (stage.isEffect()) {
						if (auxIdx < DSPManager.getMaxSends()) {
							viscera$updateEffect(src, stage, auxIdx, tracked);
							auxIdx++;
						}
					} else {
						viscera$updateFilter(src, stage, wet, tracked);
					}
				}
			}

			int err = AL10.alGetError();
			if (err != AL10.AL_NO_ERROR) {
				LOG.error("OpenAL Error [{}]: Pipeline failed for sound {}", err, sound.getLocation());
			}
		});
	}

	@Unique
	private void viscera$updateEffect(
			int src,
			DSPProcessor.Stage stage,
			int idx,
			boolean exists
	) {
		int eid = exists ? DSPManager.getTrackedId(src, DSPResource.Type.EFFECT, idx)
				: DSPManager.acquireEffect(stage.alHandle());
		int sid = exists ? DSPManager.getTrackedId(src, DSPResource.Type.SLOT, idx)
				: DSPManager.acquireSlot();

		if (!exists) {
			DSPManager.track(src, eid, DSPResource.Type.EFFECT);
			DSPManager.track(src, sid, DSPResource.Type.SLOT);
		}

		stage.params().forEach((k, v) -> {
			int key = DSPRegistry.getParamKey(k);
			if (key != 0) {
				if (isIntegerParam(k)) {
					EXTEfx.alEffecti(eid, key, v.intValue());
				} else {
					EXTEfx.alEffectf(eid, key, v);
				}
			}
		});

		EXTEfx.alAuxiliaryEffectSloti(sid, EXTEfx.AL_EFFECTSLOT_EFFECT, eid);
		AL11.alSource3i(src, EXTEfx.AL_AUXILIARY_SEND_FILTER, sid, idx, EXTEfx.AL_FILTER_NULL);
	}

	@Unique
	private boolean isIntegerParam(String name) {
		return name.endsWith("waveform") ||
				name.endsWith("phase") ||
				name.contains("phoneme") ||
				name.contains("tune") ||
				name.endsWith("hflimit") ||
				name.endsWith("onoff") ||
				name.contains("direction");
	}

	@Unique
	private void viscera$updateFilter(
			int src,
			DSPProcessor.Stage stage,
			float wet,
			boolean exists
	) {
		int fid = exists ? DSPManager.getTrackedId(src, DSPResource.Type.FILTER, 0)
				: DSPManager.acquireFilter(stage.alHandle());
		if (!exists) {
			DSPManager.track(src, fid, DSPResource.Type.FILTER);
		}

		stage.params().forEach((k, v) -> {
			int key = DSPRegistry.getParamKey(k);
			if (key == 0) return;

			float interpolatedVal = 1.0f - (wet * (1.0f - v));
			EXTEfx.alFilterf(fid, key, interpolatedVal);
		});

		AL10.alSourcei(src, EXTEfx.AL_DIRECT_FILTER, fid);
	}
}
