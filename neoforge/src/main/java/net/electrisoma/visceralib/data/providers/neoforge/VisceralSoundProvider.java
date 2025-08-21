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

            if (variants.isEmpty()) {
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
                        soundsArray.add(createSoundJson(soundPath, SoundEventBuilder.SoundType.FILE));
                    } else {
                        System.err.println("Missing variant sound: " + soundPath + ".ogg");
                    }
                }
            }

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
        sound.addProperty("name", modId + ":" + path);
        sound.addProperty("type", type.getId());
        return sound;
    }

    private boolean exists(String soundPath) {
        ResourceLocation loc = VisceraLib.path(modId, soundPath);
        return helper.exists(loc, PackType.CLIENT_RESOURCES, ".ogg", "sounds");
    }
}
