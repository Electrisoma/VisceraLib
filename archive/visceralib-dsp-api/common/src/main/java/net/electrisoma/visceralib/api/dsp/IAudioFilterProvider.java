package net.electrisoma.visceralib.api.dsp;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IAudioFilterProvider {

    @Nullable AudioFilter getFilter(SoundInstance sound, LocalPlayer player, ClientLevel level);
}