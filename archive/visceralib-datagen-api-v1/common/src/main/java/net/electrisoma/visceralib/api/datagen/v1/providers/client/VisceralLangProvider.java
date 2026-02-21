package net.electrisoma.visceralib.api.datagen.v1.providers.client;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.core.utils.TextUtils;
import net.electrisoma.visceralib.platform.datagen.v1.client.services.IDatagenClientHelper;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import com.google.gson.JsonObject;

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

	public VisceralLangProvider(
			PackOutput output,
			String modid,
			String locale,
			CompletableFuture<HolderLookup.Provider> lookupProvider
	) {
		this(output, modid, locale, lookupProvider, "en_us".equals(locale));
	}

	public VisceralLangProvider(
			PackOutput output,
			String modid,
			String locale,
			CompletableFuture<HolderLookup.Provider> lookupProvider,
			boolean generateUpsideDown
	) {
		this.output = output;
		this.modid = modid;
		this.locale = locale;
		this.lookupProvider = lookupProvider;
		this.generateUpsideDown = generateUpsideDown;
	}

	protected abstract void generateTranslations(HolderLookup.Provider lookup, TranslationBuilder builder);

	@Override
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

	private CompletableFuture<?> save(
			CachedOutput writer,
			TreeMap<String, String> entries,
			String code
	) {
		JsonObject json = new JsonObject();
		entries.forEach(json::addProperty);

		Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
				.resolve(this.modid)
				.resolve("lang")
				.resolve(code + ".json");

		return DataProvider.saveStable(writer, json, path);
	}

	@Override
	public String getName() {
		return "VisceralLangProvider: " + locale + " for " + modid;
	}

	@FunctionalInterface
	public interface TranslationBuilder {

		void add(String key, String value);

		default void addAuto(Block block) {
			add(block, TextUtils.toTitleCase(RLUtils.getPathOrDefault(BuiltInRegistries.BLOCK.getKey(block), "unknown")));
		}

		default void addAuto(Item item) {
			add(item, TextUtils.toTitleCase(RLUtils.getPathOrDefault(BuiltInRegistries.ITEM.getKey(item), "unknown")));
		}

		default void addAuto(EntityType<?> type) {
			add(type, TextUtils.toTitleCase(RLUtils.getPathOrDefault(BuiltInRegistries.ENTITY_TYPE.getKey(type), "unknown")));
		}

		default void addAuto(MobEffect effect) {
			add(effect, TextUtils.toTitleCase(RLUtils.getPathOrDefault(BuiltInRegistries.MOB_EFFECT.getKey(effect), "unknown")));
		}

		default void addAuto(TagKey<?> tag) {
			add(tag, TextUtils.toTitleCase(RLUtils.getPathOrDefault(tag.location(), "unknown")));
		}

		default void addAuto(ResourceKey<Enchantment> enchantment) {
			add(enchantment, TextUtils.toTitleCase(RLUtils.getPathOrDefault(enchantment.location(), "unknown")));
		}

		default void add(Block block, String value) {
			add(block.getDescriptionId(), value);
		}

		default void add(Item item, String value) {
			add(item.getDescriptionId(), value);
		}

		default void add(ResourceKey<Enchantment> enchantment, String value) {
			String key = Util.makeDescriptionId("enchantment", enchantment.location());
			add(key, value);
		}

		default void add(EntityType<?> type, String value) {
			add(type.getDescriptionId(), value);
		}

		default void add(MobEffect effect, String value) {
			add(effect.getDescriptionId(), value);
		}

		default void add(SoundEvent sound, String value) {
			add("subtitles." + sound.getLocation().toLanguageKey(), value);
		}

		default void add(TagKey<?> tag, String value) {
			String key = IDatagenClientHelper.INSTANCE.getTagTranslationKey(tag);
			add(key, value);
		}

		default void add(ResourceLocation id, String value) {
			add(id.toLanguageKey(), value);
		}
	}
}
