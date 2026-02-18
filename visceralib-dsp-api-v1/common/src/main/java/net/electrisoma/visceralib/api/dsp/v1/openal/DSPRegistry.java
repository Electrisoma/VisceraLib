package net.electrisoma.visceralib.api.dsp.v1.openal;

import net.electrisoma.visceralib.api.dsp.v1.data.DSPProcessor;

import net.minecraft.resources.ResourceLocation;

import org.lwjgl.openal.EXTEfx;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DSPRegistry {

	private static final Map<ResourceLocation, DSPProcessor> TEMPLATES = new HashMap<>();
	private static final Map<String, Integer> PARAM_MAP = new HashMap<>();

	public record TypeResult(int id, boolean isEffect) {}

	public static void updateTemplates(Map<ResourceLocation, DSPProcessor> newTemplates) {
		TEMPLATES.clear();
		TEMPLATES.putAll(newTemplates);
	}

	public static DSPProcessor getTemplate(ResourceLocation loc) {
		return TEMPLATES.get(loc);
	}

	public static int getParamKey(String name) {
		return PARAM_MAP.getOrDefault(name.toLowerCase(Locale.ROOT), 0);
	}

	public static TypeResult resolveType(String typeName) {
		String name = typeName.toLowerCase(Locale.ROOT);
		int effectId = switch (name) {
			case "reverb" -> EXTEfx.AL_EFFECT_REVERB;
			case "chorus" -> EXTEfx.AL_EFFECT_CHORUS;
			case "distortion" -> EXTEfx.AL_EFFECT_DISTORTION;
			case "echo" -> EXTEfx.AL_EFFECT_ECHO;
			case "flanger" -> EXTEfx.AL_EFFECT_FLANGER;
			case "frequency_shifter" -> EXTEfx.AL_EFFECT_FREQUENCY_SHIFTER;
			case "vocal_morpher" -> EXTEfx.AL_EFFECT_VOCAL_MORPHER;
			case "pitch_shifter" -> EXTEfx.AL_EFFECT_PITCH_SHIFTER;
			case "ring_modulator" -> EXTEfx.AL_EFFECT_RING_MODULATOR;
			case "autowah" -> EXTEfx.AL_EFFECT_AUTOWAH;
			case "compressor" -> EXTEfx.AL_EFFECT_COMPRESSOR;
			case "equalizer" -> EXTEfx.AL_EFFECT_EQUALIZER;
			case "eax_reverb" -> EXTEfx.AL_EFFECT_EAXREVERB;
			default -> 0;
		};
		if (effectId != 0) return new TypeResult(effectId, true);

		int filterId = switch (name) {
			case "lowpass" -> EXTEfx.AL_FILTER_LOWPASS;
			case "highpass" -> EXTEfx.AL_FILTER_HIGHPASS;
			case "bandpass" -> EXTEfx.AL_FILTER_BANDPASS;
			default -> 0;
		};
		if (filterId != 0) return new TypeResult(filterId, false);
		return null;
	}

	static {

		// Reverb
		PARAM_MAP.put("reverb_density", EXTEfx.AL_REVERB_DENSITY);
		PARAM_MAP.put("reverb_diffusion", EXTEfx.AL_REVERB_DIFFUSION);
		PARAM_MAP.put("reverb_gain", EXTEfx.AL_REVERB_GAIN);
		PARAM_MAP.put("reverb_gainhf", EXTEfx.AL_REVERB_GAINHF);
		PARAM_MAP.put("reverb_decay_time", EXTEfx.AL_REVERB_DECAY_TIME);
		PARAM_MAP.put("reverb_decay_hfratio", EXTEfx.AL_REVERB_DECAY_HFRATIO);
		PARAM_MAP.put("reverb_reflections_gain", EXTEfx.AL_REVERB_REFLECTIONS_GAIN);
		PARAM_MAP.put("reverb_reflections_delay", EXTEfx.AL_REVERB_REFLECTIONS_DELAY);
		PARAM_MAP.put("reverb_late_reverb_gain", EXTEfx.AL_REVERB_LATE_REVERB_GAIN);
		PARAM_MAP.put("reverb_late_reverb_delay", EXTEfx.AL_REVERB_LATE_REVERB_DELAY);
		PARAM_MAP.put("reverb_air_absorption_gainhf", EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF);
		PARAM_MAP.put("reverb_room_rolloff_factor", EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR);
		PARAM_MAP.put("reverb_decay_hflimit", EXTEfx.AL_REVERB_DECAY_HFLIMIT);

		// EAX Reverb
		PARAM_MAP.put("eaxreverb_density", EXTEfx.AL_EAXREVERB_DENSITY);
		PARAM_MAP.put("eaxreverb_diffusion", EXTEfx.AL_EAXREVERB_DIFFUSION);
		PARAM_MAP.put("eaxreverb_gain", EXTEfx.AL_EAXREVERB_GAIN);
		PARAM_MAP.put("eaxreverb_gainhf", EXTEfx.AL_EAXREVERB_GAINHF);
		PARAM_MAP.put("eaxreverb_gainlf", EXTEfx.AL_EAXREVERB_GAINLF);
		PARAM_MAP.put("eaxreverb_decay_time", EXTEfx.AL_EAXREVERB_DECAY_TIME);
		PARAM_MAP.put("eaxreverb_decay_hfratio", EXTEfx.AL_EAXREVERB_DECAY_HFRATIO);
		PARAM_MAP.put("eaxreverb_decay_lfratio", EXTEfx.AL_EAXREVERB_DECAY_LFRATIO);
		PARAM_MAP.put("eaxreverb_reflections_gain", EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN);
		PARAM_MAP.put("eaxreverb_reflections_delay", EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY);
		PARAM_MAP.put("eaxreverb_reflections_pan", EXTEfx.AL_EAXREVERB_REFLECTIONS_PAN);
		PARAM_MAP.put("eaxreverb_late_reverb_gain", EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN);
		PARAM_MAP.put("eaxreverb_late_reverb_delay", EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY);
		PARAM_MAP.put("eaxreverb_late_reverb_pan", EXTEfx.AL_EAXREVERB_LATE_REVERB_PAN);
		PARAM_MAP.put("eaxreverb_echo_time", EXTEfx.AL_EAXREVERB_ECHO_TIME);
		PARAM_MAP.put("eaxreverb_echo_depth", EXTEfx.AL_EAXREVERB_ECHO_DEPTH);
		PARAM_MAP.put("eaxreverb_modulation_time", EXTEfx.AL_EAXREVERB_MODULATION_TIME);
		PARAM_MAP.put("eaxreverb_modulation_depth", EXTEfx.AL_EAXREVERB_MODULATION_DEPTH);
		PARAM_MAP.put("eaxreverb_air_absorption_gainhf", EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF);
		PARAM_MAP.put("eaxreverb_hfreference", EXTEfx.AL_EAXREVERB_HFREFERENCE);
		PARAM_MAP.put("eaxreverb_lfreference", EXTEfx.AL_EAXREVERB_LFREFERENCE);
		PARAM_MAP.put("eaxreverb_room_rolloff_factor", EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR);
		PARAM_MAP.put("eaxreverb_decay_hflimit", EXTEfx.AL_EAXREVERB_DECAY_HFLIMIT);

		// Chorus
		PARAM_MAP.put("chorus_waveform", EXTEfx.AL_CHORUS_WAVEFORM);
		PARAM_MAP.put("chorus_phase", EXTEfx.AL_CHORUS_PHASE);
		PARAM_MAP.put("chorus_rate", EXTEfx.AL_CHORUS_RATE);
		PARAM_MAP.put("chorus_depth", EXTEfx.AL_CHORUS_DEPTH);
		PARAM_MAP.put("chorus_feedback", EXTEfx.AL_CHORUS_FEEDBACK);
		PARAM_MAP.put("chorus_delay", EXTEfx.AL_CHORUS_DELAY);

		// Distortion
		PARAM_MAP.put("distortion_edge", EXTEfx.AL_DISTORTION_EDGE);
		PARAM_MAP.put("distortion_gain", EXTEfx.AL_DISTORTION_GAIN);
		PARAM_MAP.put("distortion_lowpass_cutoff", EXTEfx.AL_DISTORTION_LOWPASS_CUTOFF);
		PARAM_MAP.put("distortion_eqcenter", EXTEfx.AL_DISTORTION_EQCENTER);
		PARAM_MAP.put("distortion_eqbandwidth", EXTEfx.AL_DISTORTION_EQBANDWIDTH);

		// Echo
		PARAM_MAP.put("echo_delay", EXTEfx.AL_ECHO_DELAY);
		PARAM_MAP.put("echo_lrdelay", EXTEfx.AL_ECHO_LRDELAY);
		PARAM_MAP.put("echo_damping", EXTEfx.AL_ECHO_DAMPING);
		PARAM_MAP.put("echo_feedback", EXTEfx.AL_ECHO_FEEDBACK);
		PARAM_MAP.put("echo_spread", EXTEfx.AL_ECHO_SPREAD);

		// Flanger
		PARAM_MAP.put("flanger_waveform", EXTEfx.AL_FLANGER_WAVEFORM);
		PARAM_MAP.put("flanger_phase", EXTEfx.AL_FLANGER_PHASE);
		PARAM_MAP.put("flanger_rate", EXTEfx.AL_FLANGER_RATE);
		PARAM_MAP.put("flanger_depth", EXTEfx.AL_FLANGER_DEPTH);
		PARAM_MAP.put("flanger_feedback", EXTEfx.AL_FLANGER_FEEDBACK);
		PARAM_MAP.put("flanger_delay", EXTEfx.AL_FLANGER_DELAY);

		// Frequency shifter
		PARAM_MAP.put("frequency_shifter_frequency", EXTEfx.AL_FREQUENCY_SHIFTER_FREQUENCY);
		PARAM_MAP.put("frequency_shifter_left_direction", EXTEfx.AL_FREQUENCY_SHIFTER_LEFT_DIRECTION);
		PARAM_MAP.put("frequency_shifter_right_direction", EXTEfx.AL_FREQUENCY_SHIFTER_RIGHT_DIRECTION);

		// VocMorpher
		PARAM_MAP.put("vocmorpher_phonemea", EXTEfx.AL_VOCMORPHER_PHONEMEA);
		PARAM_MAP.put("vocmorpher_phonemea_coarse_tuning", EXTEfx.AL_VOCMORPHER_PHONEMEA_COARSE_TUNING);
		PARAM_MAP.put("vocmorpher_phonemeb", EXTEfx.AL_VOCMORPHER_PHONEMEB);
		PARAM_MAP.put("vocmorpher_phonemeb_coarse_tuning", EXTEfx.AL_VOCMORPHER_PHONEMEB_COARSE_TUNING);
		PARAM_MAP.put("vocmorpher_waveform", EXTEfx.AL_VOCMORPHER_WAVEFORM);
		PARAM_MAP.put("vocmorpher_rate", EXTEfx.AL_VOCMORPHER_RATE);

		// Pitch shifter
		PARAM_MAP.put("pitch_shifter_coarse_tune", EXTEfx.AL_PITCH_SHIFTER_COARSE_TUNE);
		PARAM_MAP.put("pitch_shifter_fine_tune", EXTEfx.AL_PITCH_SHIFTER_FINE_TUNE);

		// Ring modulator
		PARAM_MAP.put("ring_modulator_frequency", EXTEfx.AL_RING_MODULATOR_FREQUENCY);
		PARAM_MAP.put("ring_modulator_highpass_cutoff", EXTEfx.AL_RING_MODULATOR_HIGHPASS_CUTOFF);
		PARAM_MAP.put("ring_modulator_waveform", EXTEfx.AL_RING_MODULATOR_WAVEFORM);

		// AutoWah
		PARAM_MAP.put("autowah_attack_time", EXTEfx.AL_AUTOWAH_ATTACK_TIME);
		PARAM_MAP.put("autowah_release_time", EXTEfx.AL_AUTOWAH_RELEASE_TIME);
		PARAM_MAP.put("autowah_resonance", EXTEfx.AL_AUTOWAH_RESONANCE);
		PARAM_MAP.put("autowah_peak_gain", EXTEfx.AL_AUTOWAH_PEAK_GAIN);

		// Compressor
		PARAM_MAP.put("compressor_onoff", EXTEfx.AL_COMPRESSOR_ONOFF);

		// EQ
		PARAM_MAP.put("equalizer_low_gain", EXTEfx.AL_EQUALIZER_LOW_GAIN);
		PARAM_MAP.put("equalizer_low_cutoff", EXTEfx.AL_EQUALIZER_LOW_CUTOFF);
		PARAM_MAP.put("equalizer_mid1_gain", EXTEfx.AL_EQUALIZER_MID1_GAIN);
		PARAM_MAP.put("equalizer_mid1_center", EXTEfx.AL_EQUALIZER_MID1_CENTER);
		PARAM_MAP.put("equalizer_mid1_width", EXTEfx.AL_EQUALIZER_MID1_WIDTH);
		PARAM_MAP.put("equalizer_mid2_gain", EXTEfx.AL_EQUALIZER_MID2_GAIN);
		PARAM_MAP.put("equalizer_mid2_center", EXTEfx.AL_EQUALIZER_MID2_CENTER);
		PARAM_MAP.put("equalizer_mid2_width", EXTEfx.AL_EQUALIZER_MID2_WIDTH);
		PARAM_MAP.put("equalizer_high_gain", EXTEfx.AL_EQUALIZER_HIGH_GAIN);
		PARAM_MAP.put("equalizer_high_cutoff", EXTEfx.AL_EQUALIZER_HIGH_CUTOFF);

		// Filters
		PARAM_MAP.put("lowpass_gain", EXTEfx.AL_LOWPASS_GAIN);
		PARAM_MAP.put("lowpass_gainhf", EXTEfx.AL_LOWPASS_GAINHF);

		PARAM_MAP.put("highpass_gain", EXTEfx.AL_HIGHPASS_GAIN);
		PARAM_MAP.put("highpass_gainlf", EXTEfx.AL_HIGHPASS_GAINLF);

		PARAM_MAP.put("bandpass_gain", EXTEfx.AL_BANDPASS_GAIN);
		PARAM_MAP.put("bandpass_gainlf", EXTEfx.AL_BANDPASS_GAINLF);
		PARAM_MAP.put("bandpass_gainhf", EXTEfx.AL_BANDPASS_GAINHF);
	}
}
