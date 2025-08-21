package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record SoundEntry(VisceralRegistrySupplier<SoundEvent> supplier, @Nullable String subtitle, @Nullable String soundPath) {
    public SoundEvent get() {
        return supplier.get();
    }

    public Optional<String> subtitleKey() {
        ResourceLocation id = BuiltInRegistries.SOUND_EVENT.getKey(supplier.get());
        assert id != null;
        return Optional.of("subtitles." + id.getNamespace() + "." + id.getPath());
    }

    public Optional<String> getSoundPath() {
        return Optional.ofNullable(soundPath);
    }
}
