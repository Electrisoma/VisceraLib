package net.electrisoma.visceralib.mixin.dsp.v1.client;

import net.electrisoma.visceralib.api.dsp.v1.AudioFilterHandler;
import net.electrisoma.visceralib.api.dsp.v1.AudioFilterLoader;
import net.electrisoma.visceralib.api.dsp.v1.AudioFilterManager;
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
	private final static Logger LOG = LoggerFactory.getLogger("VisceraLibAudioEffectsPipeline/SoundEngineMixin");

	@Inject(
			method = "loadLibrary",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Listener;reset()V")
	)
	private void initAudioFilters(CallbackInfo ci) {
		AudioFilterManager.reinitialize(AudioFilterLoader.getFilters());
	}

	@Inject(
			method = { "destroy", "emergencyShutdown" },
			at = @At("HEAD")
	)
	private void disposeAudioFilters(CallbackInfo ci) {
		AudioFilterManager.dispose();
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
		if (sound != null) viscera$executeFilterApplication(channelHandle, sound);
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
			viscera$executeFilterApplication(channelHandle, entry.getKey());
		return channelHandle;
	}

	@Unique
	private void viscera$executeFilterApplication(ChannelAccess.ChannelHandle channelHandle, SoundInstance sound) {
		AudioFilterHandler.PipelineResult config = AudioFilterHandler.determineConfiguration(sound);

		channelHandle.execute(channel -> {
			int sourceId = ((ChannelAccessor) channel).getSourceId();
			if (sourceId <= 0) return;

			AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, EXTEfx.AL_FILTER_NULL);
			int maxHardwareSends = AudioFilterManager.getMaxSends();
			for (int i = 0; i < maxHardwareSends; i++) {
				AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, i, EXTEfx.AL_FILTER_NULL);
			}

			if (config.chains().isEmpty()) return;

			int auxSendIndex = 0;

			for (var chain : config.chains()) {
				float eventWetness = chain.wetness();

				for (var stage : chain.template().stages()) {
					float totalWetness = eventWetness * stage.defaultWetness();

					if (totalWetness <= 0.001f) continue;

					if (stage.isEffect()) {
						if (auxSendIndex >= maxHardwareSends) continue;

						int effectId = AudioFilterManager.acquireEffect(stage.alHandle());
						int slotId = AudioFilterManager.acquireSlot();
						int sendFilterId = AudioFilterManager.acquireFilter(EXTEfx.AL_FILTER_LOWPASS);

						stage.params().forEach((name, val) -> {
							int key = AudioFilterManager.getParamKey(name);
							if (key != 0) EXTEfx.alEffectf(effectId, key, val);
						});

						EXTEfx.alAuxiliaryEffectSloti(slotId, EXTEfx.AL_EFFECTSLOT_EFFECT, effectId);

						EXTEfx.alFilterf(sendFilterId, EXTEfx.AL_LOWPASS_GAIN, totalWetness);

						AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, slotId, auxSendIndex, sendFilterId);

						auxSendIndex++;
					} else {
						int filterId = AudioFilterManager.acquireFilter(stage.alHandle());

						stage.params().forEach((name, val) -> {
							int key = AudioFilterManager.getParamKey(name);
							if (key != 0) {
								float interpolatedVal = 1.0f - (totalWetness * (1.0f - val));
								EXTEfx.alFilterf(filterId, key, interpolatedVal);
							}
						});

						AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, filterId);
					}
				}
			}

			int err = AL10.alGetError();
			if (err != AL10.AL_NO_ERROR) {
				LOG.error("OpenAL Error [{}]: Pipeline failed for sound {}", err, sound.getLocation());
			}
		});
	}
}
