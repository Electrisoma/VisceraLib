package net.electrisoma.visceralib.api.dsp;

import net.minecraft.resources.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.*;

public class AudioFilterManager {

    private static final Logger LOG = LoggerFactory.getLogger("VisceraLib-DSP/AudioManager");

    private static final Map<ResourceLocation, Integer> FILTER_IDS = new HashMap<>();
    private static final Map<ResourceLocation, int[]> STEREO_SLOT_IDS = new HashMap<>();
    private static final List<Integer> EFFECTS_TO_FREE = new ArrayList<>();
    private static int dryMixerFilter = AL10.AL_NONE;
    private static int wetMixerFilter = AL10.AL_NONE;
    private static boolean attemptedInit = false;
    private static int maxSends = 1;

    public static void attemptInitialize() {
        if (attemptedInit || !checkEfxSupport()) return;
        attemptedInit = true;

        AL10.alGetError();

        long device = ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext());

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer sends = stack.mallocInt(1);
            ALC10.alcGetIntegerv(device, EXTEfx.ALC_MAX_AUXILIARY_SENDS, sends);
            maxSends = sends.get(0);
        } catch (Exception e) {
            maxSends = 1;
        }

        LOG.info("OpenAL EFX initialized. Max Auxiliary Sends: {}", maxSends);

        dryMixerFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(dryMixerFilter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);

        wetMixerFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(wetMixerFilter, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);

        AudioFilterRegistry.getRegisteredFilters().forEach((location, filter) -> {
            if (filter.isEffect()) {
                int effectId = EXTEfx.alGenEffects();
                EXTEfx.alEffecti(effectId, EXTEfx.AL_EFFECT_TYPE, filter.type());
                filter.settings().accept(effectId);

                int slotL = EXTEfx.alGenAuxiliaryEffectSlots();
                int slotR = EXTEfx.alGenAuxiliaryEffectSlots();

                for (int i = 0; i < 2; i++) {
                    int slot = (i == 0) ? slotL : slotR;
                    EXTEfx.alAuxiliaryEffectSlotf(slot, EXTEfx.AL_EFFECTSLOT_GAIN, 1.0f);
                    EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_FALSE);
                    EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_EFFECT, effectId);
                }

                STEREO_SLOT_IDS.put(location, new int[]{slotL, slotR});
                EFFECTS_TO_FREE.add(effectId);
            } else {
                int filterId = EXTEfx.alGenFilters();
                EXTEfx.alFilteri(filterId, EXTEfx.AL_FILTER_TYPE, filter.type());
                filter.settings().accept(filterId);
                FILTER_IDS.put(location, filterId);
            }
        });
    }

    public static int getFilterId(ResourceLocation loc) { return FILTER_IDS.getOrDefault(loc, AL10.AL_NONE); }
    public static int[] getStereoSlots(ResourceLocation loc) { return STEREO_SLOT_IDS.get(loc); }
    public static int getDryMixerFilter() { return dryMixerFilter; }
    public static int getWetMixerFilter() { return wetMixerFilter; }
    public static int getMaxSends() { return maxSends; }

    private static boolean checkEfxSupport() {
        long device = ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext());
        return device != 0 && ALC.getCapabilities().ALC_EXT_EFX;
    }

    public static void dispose() {
        FILTER_IDS.values().forEach(EXTEfx::alDeleteFilters);
        STEREO_SLOT_IDS.values().forEach(slots -> Arrays.stream(slots).forEach(EXTEfx::alDeleteAuxiliaryEffectSlots));
        EFFECTS_TO_FREE.forEach(EXTEfx::alDeleteEffects);
        if (dryMixerFilter != AL10.AL_NONE) EXTEfx.alDeleteFilters(dryMixerFilter);
        FILTER_IDS.clear(); STEREO_SLOT_IDS.clear();
        EFFECTS_TO_FREE.clear();
        attemptedInit = false;
    }
}