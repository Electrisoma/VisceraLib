package net.electrisoma.visceralib.impl.dsp.client;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.api.dsp.AudioFilterRegistry;
import net.electrisoma.visceralib.impl.dsp.Constants;
import net.minecraft.sounds.SoundSource;
import org.lwjgl.openal.EXTEfx;

public final class ClientEntrypoint {

    public static void init() {
        EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);
        testFilters();
    }

    private static void testFilters() {

        var dsp = AudioFilterRegistry.forMod(Constants.MOD_ID);

//        dsp.builder("underwater_flange", EXTEfx.AL_EFFECT_FLANGER)
//                .priority(100)
//                .when((sound, player) -> player.isUnderWater())
//                .deny(SoundSource.MASTER, SoundSource.MUSIC, SoundSource.WEATHER)
//                .param(EXTEfx.AL_FLANGER_WAVEFORM, EXTEfx.AL_FLANGER_WAVEFORM_TRIANGLE)
//                .param(EXTEfx.AL_FLANGER_PHASE, 0)
//                .param(EXTEfx.AL_FLANGER_RATE, 2.0f)
//                .param(EXTEfx.AL_FLANGER_DEPTH, 1.0f)
//                .param(EXTEfx.AL_FLANGER_FEEDBACK, 0.7f)
//                .param(EXTEfx.AL_FLANGER_DELAY, 0.004f)
//                .build();

//        dsp.builder("cave_reverb", EXTEfx.AL_EFFECT_REVERB)
//                .slot(1)
//                .wetness(0.4f)
//                .width(0.0f)
//                .isolate(SoundSource.RECORDS)
//                .param(EXTEfx.AL_REVERB_DENSITY, 1.0f)
//                .param(EXTEfx.AL_REVERB_DIFFUSION, 1.0f)
//                .param(EXTEfx.AL_REVERB_GAIN, 0.32f)
//                .param(EXTEfx.AL_REVERB_DECAY_TIME, 2.5f)
//                .param(EXTEfx.AL_REVERB_LATE_REVERB_GAIN, 1.26f)
//                .build();

//        dsp.builder("record_grit", EXTEfx.AL_EFFECT_PITCH_SHIFTER)
//                .slot(1)
//                .wetness(1.0f)
//                .isolate(SoundSource.RECORDS)
//                .param(EXTEfx.AL_PITCH_SHIFTER_COARSE_TUNE, -3)
//                .build();

//        dsp.builder("record_grit", EXTEfx.AL_EFFECT_DISTORTION)
//                .slot(1)
//                .width(0.0f)
//                .wetness(0.5f)
//                .isolate(SoundSource.RECORDS)
//                .param(EXTEfx.AL_DISTORTION_GAIN, 1.0f)
//                .param(EXTEfx.AL_DISTORTION_EDGE, 0.15f)
//                .build();
//
//        dsp.builder("record_thin", EXTEfx.AL_FILTER_HIGHPASS)
//                .slot(2)
//                .width(0.0f)
//                .wetness(1.0f)
//                .isolate(SoundSource.RECORDS)
//                .param(EXTEfx.AL_HIGHPASS_GAIN, 1.0f)
//                .param(EXTEfx.AL_HIGHPASS_GAINLF, 0.01f)
//                .build();

        dsp.filter("underwater_lp", EXTEfx.AL_FILTER_LOWPASS)
                .slot(4)
                .global()
                .wetness(1.0f)
                .when((sound, player) -> player.isUnderWater())
                .deny(SoundSource.MASTER, SoundSource.MUSIC, SoundSource.WEATHER)
                .param(EXTEfx.AL_LOWPASS_GAIN, 1.0f)
                .param(EXTEfx.AL_LOWPASS_GAINHF, 0.05f)
                .build();

        dsp.effect("underwater_echo", EXTEfx.AL_EFFECT_REVERB)
                .slot(3)
                .global()
                .width(0.0f)
                .wetness(0.5f)
                .when((sound, player) -> player.isUnderWater())
                .deny(SoundSource.MASTER, SoundSource.MUSIC, SoundSource.WEATHER)
                .param(EXTEfx.AL_REVERB_DENSITY, 1.0f)
                .param(EXTEfx.AL_REVERB_DIFFUSION, 0.05f)
                .build();
    }
}