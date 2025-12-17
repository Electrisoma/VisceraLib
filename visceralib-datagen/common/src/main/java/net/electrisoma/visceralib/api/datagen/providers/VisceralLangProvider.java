package net.electrisoma.visceralib.api.datagen.providers;

import com.google.gson.JsonObject;
import net.electrisoma.visceralib.api.core.utils.TextUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class VisceralLangProvider implements DataProvider {

    private final PackOutput output;
    private final String modid;
    private final String locale;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final boolean generateUpsideDown;

    public VisceralLangProvider(PackOutput output, String modid, String locale, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.modid = modid;
        this.locale = locale;
        this.lookupProvider = lookupProvider;
        this.generateUpsideDown = "en_us".equals(locale);
    }

    public abstract void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder);

    @Override @NotNull
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.lookupProvider.thenCompose(lookup -> {
            TreeMap<String, String> mainEntries = new TreeMap<>();
            TreeMap<String, String> upsideDownEntries = new TreeMap<>();

            generateTranslations(lookup, (key, value) -> {
                if (mainEntries.put(key, value) != null)
                    throw new IllegalStateException("Duplicate translation key: " + key);
                if (generateUpsideDown)
                    upsideDownEntries.put(key, TextUtils.toUpsideDown(value));
            });

            List<CompletableFuture<?>> futures = new ArrayList<>();
            futures.add(save(writer, mainEntries, this.locale));

            if (generateUpsideDown && !upsideDownEntries.isEmpty())
                futures.add(save(writer, upsideDownEntries, "en_ud"));

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private CompletableFuture<?> save(CachedOutput writer, TreeMap<String, String> entries, String code) {
        JsonObject json = new JsonObject();
        entries.forEach(json::addProperty);

        Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(this.modid)
                .resolve("lang")
                .resolve(code + ".json");

        return DataProvider.saveStable(writer, json, path);
    }

    @Override @NotNull
    public String getName() {
        return "VisceralLangProvider: " + locale + " for " + modid;
    }

    @FunctionalInterface
    public interface TranslationBuilder {

        void add(String key, String value);

        default void addAuto(Block block) {
            add(block, TextUtils.toTitleCase(BuiltInRegistries.BLOCK.getKey(block).getPath()));
        }

        default void addAuto(Item item) {
            add(item, TextUtils.toTitleCase(BuiltInRegistries.ITEM.getKey(item).getPath()));
        }

        default void addAuto(EntityType<?> type) {
            add(type, TextUtils.toTitleCase(BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath()));
        }

        default void add(Block block, String value) {
            add(block.getDescriptionId(), value);
        }

        default void add(Item item, String value) {
            add(item.getDescriptionId(), value);
        }

        default void add(EntityType<?> type, String value) {
            add(type.getDescriptionId(), value);
        }

        default void add(MobEffect effect, String value) {
            add(effect.getDescriptionId(), value);
        }

        default void add(ResourceLocation id, String value) {
            add(id.toLanguageKey(), value);
        }

        default void add(TagKey<?> tag, String value) {
            ResourceLocation registry = tag.registry().location();
            ResourceLocation id = tag.location();
            String key = "tag." + registry.getPath() + "." + id.getNamespace() + "." + id.getPath();
            add(key, value);
        }

        default void add(SoundEvent sound, String value) {
            ResourceLocation id = sound.getLocation();
            add("sounds." + id.getNamespace() + "." + id.getPath(), value);
        }
    }
}