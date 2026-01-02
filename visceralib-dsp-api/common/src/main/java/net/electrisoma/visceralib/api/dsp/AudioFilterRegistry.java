package net.electrisoma.visceralib.api.dsp;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriConsumer;
import org.lwjgl.openal.EXTEfx;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AudioFilterRegistry {

    private static final Map<ResourceLocation, AudioFilter> FILTERS = new LinkedHashMap<>();

    public static ModContext forMod(String modId) {
        return new ModContext(modId);
    }

    public record ModContext(String modId) {

        public AudioFilter.Builder builder(String name, int type) {
            return new AudioFilter.Builder(modId, name, type, isEffect(type));
        }

        public AudioFilter.Builder effect(String name, int effectType) {
            return new AudioFilter.Builder(modId, name, effectType, true);
        }

        public AudioFilter.Builder filter(String name, int filterType) {
            return new AudioFilter.Builder(modId, name, filterType, false);
        }

        private boolean isEffect(int type) {
            return type == EXTEfx.AL_EFFECT_REVERB ||
                    type == EXTEfx.AL_EFFECT_CHORUS ||
                    type == EXTEfx.AL_EFFECT_DISTORTION ||
                    type == EXTEfx.AL_EFFECT_ECHO ||
                    type == EXTEfx.AL_EFFECT_FLANGER ||
                    type == EXTEfx.AL_EFFECT_FREQUENCY_SHIFTER ||
                    type == EXTEfx.AL_EFFECT_VOCAL_MORPHER ||
                    type == EXTEfx.AL_EFFECT_PITCH_SHIFTER ||
                    type == EXTEfx.AL_EFFECT_RING_MODULATOR ||
                    type == EXTEfx.AL_EFFECT_AUTOWAH ||
                    type == EXTEfx.AL_EFFECT_COMPRESSOR ||
                    type == EXTEfx.AL_EFFECT_EQUALIZER ||
                    type == EXTEfx.AL_EFFECT_EAXREVERB;
        }
    }

    protected static void register(AudioFilter.Builder builder) {
        ResourceLocation id = RLUtils.path(builder.getModId(), builder.getName());
        AudioFilter filter = new AudioFilter(
                id,
                builder.getType(),
                id_handle -> builder.getParameters().forEach(p -> p.accept(id_handle)),
                builder.getWhitelist(),
                builder.getBlacklist(),
                builder.isEffect(),
                builder.getWetness(),
                builder.getStereoWidth(),
                builder.isGlobal()
        );
        FILTERS.put(id, filter);
        AudioFilterHandler.internalRegister(builder.getSlot(), builder.getCondition(), filter);
    }

    public static Map<ResourceLocation, AudioFilter> getRegisteredFilters() {
        return Collections.unmodifiableMap(FILTERS);
    }
}