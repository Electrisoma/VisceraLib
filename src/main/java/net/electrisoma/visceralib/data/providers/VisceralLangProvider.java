package net.electrisoma.visceralib.data.providers;

import com.google.gson.JsonObject;
import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.builders.AbstractBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VisceralLangProvider implements DataProvider {
    private final PackOutput output;
    private final String modid;
    private final String locale;
    private final Map<String, String> entries = new LinkedHashMap<>();
    private final Set<String> addedKeys = new HashSet<>();

    private final VisceralLangProvider upsideDownLang;

    public VisceralLangProvider(PackOutput output, String modid, String locale) {
        this(output, modid, locale, "en_us".equals(locale));
    }
    private VisceralLangProvider(PackOutput output, String modid, String locale, boolean createUpsideDown) {
        this.output = output;
        this.modid = modid;
        this.locale = locale;
        this.upsideDownLang = createUpsideDown ? new VisceralLangProvider(output, modid, "en_ud", false) : null;
    }

    private <T> void addLangEntries(
            List<? extends AbstractBuilder<T, ?, ?>> builders,
            Function<T, ResourceLocation> idGetter,
            String typePrefix) {

        for (AbstractBuilder<T, ?, ?> builder : builders) {
            builder.getRegisteredSupplier().ifPresent(supplier -> {
                T object = supplier.get();
                ResourceLocation id = idGetter.apply(object);
                if (!id.getNamespace().equals(modid)) return;
                String langKey = typePrefix + id.getNamespace() + "." + id.getPath();
                String langName = builder.getLangEntry().orElse(toEnglishName(id.getPath()));
                add(langKey, langName);
            });
        }
    }

    public void add(@NotNull String key, @NotNull String value) {
        if (!addedKeys.add(key)) {
            VisceraLib.LOGGER.info("Duplicate lang key detected (ignored): {}", key);
            return;
        }
        entries.put(key, value);

        if (upsideDownLang != null)
            upsideDownLang.add(key, toUpsideDown(value));
    }

    public void addBlock(Supplier<? extends Block> block) {
        addBlock(block.get(), autoName(block.get()));
    }
    public void addBlock(Block block, @NotNull String name) {
        add(block.getDescriptionId(), name);
    }

    public void addBlockWithTooltip(Supplier<? extends Block> block, String tooltip) {
        addBlock(block);
        addTooltip(block, tooltip);
    }
    public void addBlockWithTooltip(Supplier<? extends Block> block, String name, String tooltip) {
        addBlock(block.get(), name);
        addTooltip(block, tooltip);
    }

    public void addItem(Supplier<? extends Item> item) {
        addItem(item.get(), autoName(item.get()));
    }
    public void addItem(Item item, @NotNull String name) {
        add(item.getDescriptionId(), name);
    }

    public void addItemWithTooltip(Supplier<? extends Item> item, String tooltip) {
        addItem(item);
        addTooltip(item, tooltip);
    }
    public void addItemWithTooltip(Supplier<? extends Item> item, String name, String tooltip) {
        addItem(item.get(), name);
        addTooltip(item, tooltip);
    }

    public void addTooltip(Supplier<? extends ItemLike> item, String tooltip) {
        add(item.get().asItem().getDescriptionId() + ".desc", tooltip);
    }
    public void addTooltip(Supplier<? extends ItemLike> item, List<String> lines) {
        for (int i = 0; i < lines.size(); i++)
            add(item.get().asItem().getDescriptionId() + ".desc." + i, lines.get(i));
    }

    public void addEntity(Supplier<? extends EntityType<?>> entity) {
        addEntity(entity.get(), autoName(entity.get()));
    }
    public void addEntity(EntityType<?> entity, @NotNull String name) {
        add(entity.getDescriptionId(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> ench) {
        addEnchantment(ench.get(), autoName(ench.get()));
    }
    public void addEnchantment(Enchantment ench, @NotNull String name) {
        ResourceLocation key = Registries.ENCHANTMENT.registry();
        add("enchantment." + key.getNamespace() + "." + key.getPath(), name);
    }

    public void addEffect(Supplier<? extends MobEffect> effect) {
        addEffect(effect.get(), autoName(effect.get()));
    }
    public void addEffect(MobEffect effect, @NotNull String name) {
        add(effect.getDescriptionId(), name);
    }

    private String autoName(Item item) {
        return toEnglishName(BuiltInRegistries.ITEM.getKey(item).getPath());
    }
    private String autoName(Block block) {
        return toEnglishName(BuiltInRegistries.BLOCK.getKey(block).getPath());
    }
    private String autoName(EntityType<?> entity) {
        return toEnglishName(BuiltInRegistries.ENTITY_TYPE.getKey(entity).getPath());
    }
    private String autoName(Enchantment ench) {
        ResourceLocation key = Registries.ENCHANTMENT.registry();
        return toEnglishName(key.getPath());
    }
    private String autoName(MobEffect effect) {
        return toEnglishName(Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect)).getPath());
    }

    public static String toEnglishName(String id) {
        return Arrays.stream(id.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1))
                .collect(Collectors.joining(" "));
    }
    private String toUpsideDown(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            int index = NORMAL.indexOf(c);
            result.append(index >= 0 ? UPSIDE.charAt(index) : c);
        }
        return result.toString();
    }

    private static final String NORMAL =
            "abcdefghijklmnopqrstuvwxyz" +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "0123456789" +
                    ".,'?!_;/\\()[]{}<>";

    private static final String UPSIDE =
            "ɐqɔpǝɟƃɥᴉɾʞןɯuodbɹsʇnʌʍxʎz" +
                    "∀ᙠƆᗡƎℲ⅁HΙſʞWɯNOԀӨᴚS⊥∩ΛMX⅄Z" +
                    "0ƖᄅƐㄣϛ9ㄥ86" +
                    "˙‘‚¿¡‾؛/\\)(][}{><";

    public static CompletableFuture<?> saveLang(
            String modid,
            String locale,
            Map<String, String> entries,
            PackOutput output,
            CachedOutput cache) {

        Path target = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(modid)
                .resolve("lang")
                .resolve(locale + ".json");

        JsonObject json = new JsonObject();
        entries.forEach(json::addProperty);

        return DataProvider.saveStable(cache, json, target);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        VisceraLib.LOGGER.info("[VisceralLangProvider] run() called for locale {}, entries count: {}", locale, entries.size());
        CompletableFuture<?> mainSave = saveLang(modid, locale, entries, output, cache);

        if (upsideDownLang == null) {
            return mainSave;
        } else {
            CompletableFuture<?> upsideSave = upsideDownLang.run(cache);
            return CompletableFuture.allOf(mainSave, upsideSave);
        }
    }

    public String getModId() {
        return this.modid;
    }

    @Override
    public String getName() {
        return modid + " Lang (" + locale + ")";
    }
}
