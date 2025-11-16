package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.SoundEntry;
import net.electrisoma.visceralib.data.providers.VisceralLangProvider;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SoundEventBuilder<R extends AbstractVisceralRegistrar<R>>
        extends AbstractBuilder<SoundEvent, R, SoundEventBuilder<R>> {

    private static final List<SoundEventBuilder<?>> ALL_BUILDERS = new ArrayList<>();
    private final List<ConfiguredSoundEvent> wrappedEvents = new ArrayList<>();
    public record ConfiguredSoundEvent(SoundEvent event, float volume, float pitch) {}
    private final List<SoundVariant> variants = new ArrayList<>();
    public record SoundVariant(
            ResourceLocation path,
            SoundType type,
            Float volume,
            Float pitch,
            Integer weight,
            Boolean stream,
            Integer attenuationDistance,
            Boolean preload
    ) {}

    private final VisceralDeferredRegister<SoundEvent> register;
    private VisceralRegistrySupplier<SoundEvent> registeredSupplier;

    private String subtitle;
    public String soundPath;
    private SoundSource category;
    private Integer attenuationDistance;

    public SoundEventBuilder(R registrar, String name) {
        super(registrar, name);
        this.register = registrar.deferredRegister(Registries.SOUND_EVENT);
        ALL_BUILDERS.add(this);
    }

    public SoundEventBuilder<R> attenuationDistance(int distance) {
        this.attenuationDistance = distance;
        return self();
    }

    public SoundEventBuilder<R> addVariant(String path) {
        return addVariant(path, SoundType.FILE, null, null, null, null, null, null);
    }

    public SoundEventBuilder<R> addVariant(String path, SoundType type) {
        return addVariant(path, type, null, null, null, null, null, null);
    }

    public SoundEventBuilder<R> addVariant(String path,
                                           SoundType type,
                                           Float volume,
                                           Float pitch,
                                           Integer weight,
                                           Boolean stream,
                                           Integer attenuationDistance,
                                           Boolean preload) {
        variants.add(new SoundVariant(VisceraLib.path(registrar.getModId(), path), type, volume, pitch, weight, stream, attenuationDistance, preload));
        return self();
    }

    public SoundEventBuilder<R> addVariantWithAttenuation(String path, int distance) {
        return addVariant(path, SoundType.FILE, null, null, null, null, distance, null);
    }
    public SoundEventBuilder<R> addWeightedVariant(String path, int weight) {
        return addVariant(path, SoundType.FILE, null, null, weight, null, null, null);
    }

    public SoundEventBuilder<R> addVariantWithVolume(String path, float volume) {
        return addVariant(path, SoundType.FILE, volume, null, null, null, null, null);
    }

    public SoundEventBuilder<R> playExisting(SoundEvent event, float volume, float pitch) {
        wrappedEvents.add(new ConfiguredSoundEvent(event, volume, pitch));
        return self();
    }

    public SoundEventBuilder<R> category(SoundSource source) {
        this.category = source;
        return self();
    }

    public SoundEventBuilder<R> subtitle(String subtitle) {
        this.subtitle = subtitle;
        return self();
    }

    public SoundEventBuilder<R> soundPath(String path) {
        this.soundPath = path;
        return self();
    }

    public SoundEntry register() {
        ResourceLocation id = VisceraLib.path(registrar.getModId(), name);

        VisceralRegistrySupplier<SoundEvent> raw = register.register(name, () ->
                SoundEvent.createVariableRangeEvent(id)
        );

        VisceralRegistrySupplier<SoundEvent> typed = new VisceralRegistrySupplier<>(
                raw.getKey(), raw::get
        );

        typed.listen(event -> postRegisterTasks.forEach(task -> task.accept(event)));
        this.registeredSupplier = typed;

        return new SoundEntry(typed, subtitle, soundPath != null ? soundPath : name);
    }

    @Override
    public Optional<VisceralRegistrySupplier<SoundEvent>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }

    public Optional<String> getSubtitle() {
        return Optional.ofNullable(subtitle);
    }

    public static List<SoundEventBuilder<?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }
    public List<SoundVariant> getVariants() {
        return Collections.unmodifiableList(variants);
    }
    public List<ConfiguredSoundEvent> getWrappedEvents() {
        return Collections.unmodifiableList(wrappedEvents);
    }
    public Optional<SoundSource> getCategory() {
        return Optional.ofNullable(category);
    }
    public Optional<Integer> getAttenuationDistance() {
        return Optional.ofNullable(attenuationDistance);
    }

    public static void provideLang(VisceralLangProvider provider) {
        for (SoundEventBuilder<?> builder : getAllBuilders()) {
            builder.getRegisteredSupplier().ifPresent(supplier -> {
                SoundEvent sound = supplier.get();
                var id = BuiltInRegistries.SOUND_EVENT.getKey(sound);

                assert id != null;
                if (!id.getNamespace().equals(provider.getModId()))
                    return;

                String langKey =  id.getNamespace() + "." + "subtitle." + id.getPath();
                String langValue = builder.getLangEntry().orElse(VisceralLangProvider.toEnglishName(id.getPath()));

                provider.add(langKey, langValue);
            });
        }
    }

    public enum SoundType {
        FILE("file"), EVENT("event");

        private final String id;
        SoundType(String id) { this.id = id; }
        public String getId() { return id; }
    }
}
