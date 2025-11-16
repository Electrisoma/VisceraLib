package net.electrisoma.visceralib.data.providers.neoforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.builders.SoundEventBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class VisceralSoundProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final ExistingFileHelper helper;

    public VisceralSoundProvider(String modId, PackOutput output, ExistingFileHelper helper) {
        this.output = output;
        this.modId = modId;
        this.helper = helper;
    }

    @Override @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        return generate(output.getOutputFolder(), cache);
    }

    @Override @NotNull
    public String getName() {
        return modId + "Sounds";
    }

    public CompletableFuture<?> generate(Path outputPath, CachedOutput cache) {
        JsonObject root = new JsonObject();

        for (SoundEventBuilder<?> builder : SoundEventBuilder.getAllBuilders()) {
            Optional<VisceralRegistrySupplier<SoundEvent>> optionalSupplier = builder.getRegisteredSupplier();
            if (optionalSupplier.isEmpty()) continue;

            SoundEvent event = optionalSupplier.get().get();
            ResourceLocation id = BuiltInRegistries.SOUND_EVENT.getKey(event);
            assert id != null;
            if (!id.getNamespace().equals(modId)) continue;

            JsonObject soundDef = new JsonObject();
            JsonArray soundsArray = new JsonArray();

            List<SoundEventBuilder.SoundVariant> variants = builder.getVariants();
            List<SoundEventBuilder.ConfiguredSoundEvent> wrapped = builder.getWrappedEvents();

            for (SoundEventBuilder.ConfiguredSoundEvent wrappedEvent : wrapped) {
                ResourceLocation wrappedId = BuiltInRegistries.SOUND_EVENT.getKey(wrappedEvent.event());
                if (wrappedId != null) {
                    JsonObject wrappedJson = createSoundJson(wrappedId.toString(), SoundEventBuilder.SoundType.EVENT);

                    if (wrappedEvent.volume() != 1.0F)
                        wrappedJson.addProperty("volume", wrappedEvent.volume());
                    if (wrappedEvent.pitch() != 1.0F)
                        wrappedJson.addProperty("pitch", wrappedEvent.pitch());

                    soundsArray.add(wrappedJson);
                }
            }

            if (variants.isEmpty() && wrapped.isEmpty()) {
                String fallback = builder.soundPath != null ? builder.soundPath : id.getPath();
                if (exists(fallback)) {
                    soundsArray.add(createSoundJson(fallback, SoundEventBuilder.SoundType.FILE));
                } else {
                    System.err.println("Missing sound: " + fallback + ".ogg");
                    continue;
                }
            } else {
                for (SoundEventBuilder.SoundVariant variant : variants) {
                    String soundPath = variant.path().getPath();
                    if (exists(soundPath)) {
                        JsonObject soundJson = createSoundJson(soundPath, variant.type());

                        if (variant.volume() != null && variant.volume() != 1.0F)
                            soundJson.addProperty("volume", variant.volume());
                        if (variant.pitch() != null && variant.pitch() != 1.0F)
                            soundJson.addProperty("pitch", variant.pitch());
                        if (variant.weight() != null && variant.weight() != 1)
                            soundJson.addProperty("weight", variant.weight());
                        if (variant.stream() != null)
                            soundJson.addProperty("stream", variant.stream());
                        if (variant.attenuationDistance() != null)
                            soundJson.addProperty("attenuation_distance", variant.attenuationDistance());
                        if (variant.preload() != null)
                            soundJson.addProperty("preload", variant.preload());

                        soundsArray.add(soundJson);
                    } else {
                        System.err.println("Missing variant sound: " + soundPath + ".ogg");
                    }
                }
            }

            builder.getCategory().ifPresent(source ->
                    soundDef.addProperty("category", source.getName())
            );

            soundDef.add("sounds", soundsArray);

            builder.getSubtitle().ifPresent(subtitle -> {
                String subtitleKey = modId + ".subtitle." + id.getPath();
                soundDef.addProperty("subtitle", subtitleKey);
            });

            root.add(id.getPath(), soundDef);
        }

        Path path = outputPath.resolve("assets/" + modId + "/sounds.json");
        return DataProvider.saveStable(cache, root, path);
    }

    private JsonObject createSoundJson(String path, SoundEventBuilder.SoundType type) {
        JsonObject sound = new JsonObject();
        String name = type == SoundEventBuilder.SoundType.FILE ? modId + ":" + path : path;
        sound.addProperty("name", name);
        sound.addProperty("type", type.getId());
        return sound;
    }

    private boolean exists(String soundPath) {
        ResourceLocation loc = VisceraLib.path(modId, soundPath);
        return helper.exists(loc, PackType.CLIENT_RESOURCES, ".ogg", "sounds");
    }
}