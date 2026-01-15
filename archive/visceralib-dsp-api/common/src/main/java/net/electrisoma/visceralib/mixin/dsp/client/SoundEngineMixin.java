package net.electrisoma.visceralib.mixin.dsp.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.audio.Channel;
import net.electrisoma.visceralib.api.dsp.AudioFilterHandler;
import net.electrisoma.visceralib.api.dsp.AudioFilterManager;
import net.electrisoma.visceralib.mixin.dsp.client.accessor.ChannelAccessor;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
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
    private static Logger LOG = LoggerFactory.getLogger("VisceraLib-DSP/SoundEngineMixin");

    @Inject(method = "loadLibrary", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Listener;reset()V"))
    private void initAudioFilters(CallbackInfo ci) {
        AudioFilterManager.attemptInitialize();
    }

    @Inject(method = { "destroy", "emergencyShutdown" }, at = @At("HEAD"))
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
    private void viscera$executeFilterApplication(
            ChannelAccess.ChannelHandle channelHandle,
            SoundInstance sound
    ) {
        AudioFilterHandler.FilterResult config = AudioFilterHandler.determineConfiguration(sound);

        channelHandle.execute(channel -> {
            int sourceId = ((ChannelAccessor) channel).getSourceId();
            if (sourceId <= 0) return;

            AL10.alGetError();

            int maxHardwareSends = AudioFilterManager.getMaxSends();

            if (config.effectLoc() != null) {
                int[] slots = AudioFilterManager.getStereoSlots(config.effectLoc());
                if (slots != null) {

                    if (config.isGlobal()) {
                        AL10.alSourcei(sourceId, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
                        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
                        AL10.alSourcei(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER_GAIN_AUTO, AL10.AL_FALSE);
                    } else {
                        AL10.alSourcei(sourceId, AL10.AL_SOURCE_RELATIVE, AL10.AL_FALSE);
                        AL10.alSourcei(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER_GAIN_AUTO, AL10.AL_TRUE);
                    }

                    int wetFilter = AudioFilterManager.getWetMixerFilter();
                    EXTEfx.alFilterf(wetFilter, EXTEfx.AL_LOWPASS_GAIN, 1.0f - config.dryGain());

                    AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, slots[0], 0, wetFilter);

                    if (config.width() > 0.0f && maxHardwareSends > 1) {
                        AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, slots[1], 1, wetFilter);
                    } else if (maxHardwareSends > 1) {
                        AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 1, EXTEfx.AL_FILTER_NULL);
                    }

                    int dryFilter = AudioFilterManager.getDryMixerFilter();
                    EXTEfx.alFilterf(dryFilter, EXTEfx.AL_LOWPASS_GAIN, config.dryGain());
                    AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, dryFilter);
                }
            } else if (config.filterId() != AL10.AL_NONE) {
                AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 0, EXTEfx.AL_FILTER_NULL);
                if (maxHardwareSends > 1) {
                    AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 1, EXTEfx.AL_FILTER_NULL);
                }

                AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, config.filterId());
            } else {
                AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 0, EXTEfx.AL_FILTER_NULL);
                if (maxHardwareSends > 1) {
                    AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, 1, EXTEfx.AL_FILTER_NULL);
                }
                AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, EXTEfx.AL_FILTER_NULL);
            }

            int err = AL10.alGetError();
            if (err != AL10.AL_NO_ERROR) {
                LOG.error("OpenAL Error in Filter Application: {} (Source ID: {})", err, sourceId);
            }
        });
    }
}