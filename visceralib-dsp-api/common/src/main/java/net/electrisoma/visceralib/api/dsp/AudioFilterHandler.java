package net.electrisoma.visceralib.api.dsp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.AL10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;

public class AudioFilterHandler {

    private record PrioritizedProvider(int slot, IAudioFilterProvider provider) {}
    private static final List<PrioritizedProvider> PROVIDERS = new ArrayList<>();

    public record FilterResult(int filterId, @Nullable ResourceLocation effectLoc, float dryGain, float width, boolean isGlobal) {}

    static void internalRegister(int slot, BiPredicate<SoundInstance, LocalPlayer> condition, AudioFilter filter) {
        PROVIDERS.add(new PrioritizedProvider(slot, (sound, player, level) -> condition.test(sound, player) ? filter : null));
        PROVIDERS.sort(Comparator.comparingInt(PrioritizedProvider::slot));
    }

    public static FilterResult determineConfiguration(SoundInstance sound) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null)
            return new FilterResult(AL10.AL_NONE, null, 1.0f, 1.0f, false);

        int filterId = AL10.AL_NONE;
        ResourceLocation effectLoc = null;
        float dryGain = 1.0f;
        float width = 1.0f;
        boolean isGlobal = false;

        for (PrioritizedProvider entry : PROVIDERS) {
            AudioFilter filter = entry.provider().getFilter(sound, client.player, client.level);
            if (filter == null || !filter.canApplyTo(sound.getSource()))
                continue;

            if (!filter.isEffect() && filterId == AL10.AL_NONE) {
                filterId = AudioFilterManager.getFilterId(filter.getId());
            } else if (filter.isEffect() && effectLoc == null) {
                effectLoc = filter.getId();
                dryGain = 1.0f - filter.getWetness();
                width = filter.getStereoWidth();
                isGlobal = filter.isGlobal();
            }
        }
        return new FilterResult(filterId, effectLoc, dryGain, width, isGlobal);
    }
}