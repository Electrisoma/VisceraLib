package net.electrisoma.resotech.fabric.providers;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.registry.ResoTechBlocks;
import net.electrisoma.resotech.registry.ResoTechItems;
import net.electrisoma.resotech.registry.ResoTechTabs;

import io.github.fabricators_of_create.porting_lib.data.LanguageProvider;

import net.electrisoma.resotech.registry.helpers.BlockBuilder;
import net.electrisoma.resotech.registry.helpers.ItemBuilder;
import net.electrisoma.resotech.registry.helpers.TabBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Registry;

import com.google.common.base.Supplier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LangGen extends LanguageProvider {
    private final LangGen upsideDownLang;

    public LangGen(PackOutput output) {
        super(output, ResoTech.MOD_ID, "en_us");
        this.upsideDownLang = new LangGen(output, "en_ud");
    }
    private LangGen(PackOutput output, String locale) {
        super(output, ResoTech.MOD_ID, locale);
        this.upsideDownLang = null;
    }

    Set<String> addedKeys = new HashSet<>();

    @Override
    protected void addTranslations() {
        for (Block block : ResoTechBlocks.BLOCKS.getRegistrar()) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (ResoTech.MOD_ID.equals(id.getNamespace())) {
                boolean hasLang = BlockBuilder.getAllBuilders().stream()
                        .anyMatch(builder -> builder.getName().equals(id.getPath())
                                && builder.getLangEntry().isPresent());
                if (!hasLang) addBlock(() -> block);
            }
        }

        for (var builder : BlockBuilder.getAllBuilders()) {
            builder.getLangEntry().ifPresent(lang -> {
                ResourceLocation id = ResoTech.path(builder.getName());
                add("block." + id.getNamespace() + "." + id.getPath(), lang);
            });
        }

        for (Item item : ResoTechItems.ITEMS.getRegistrar()) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (ResoTech.MOD_ID.equals(id.getNamespace())) {
                boolean hasLang = ItemBuilder.getAllBuilders().stream()
                        .anyMatch(builder -> builder.getName().equals(id.getPath())
                                && builder.getLangEntry().isPresent());
                if (!hasLang) addItem(() -> item);
            }
        }

        for (var builder : ItemBuilder.getAllBuilders()) {
            builder.getLangEntry().ifPresent(lang -> {
                ResourceLocation id = ResoTech.path(builder.getName());
                add("item." + id.getNamespace() + "." + id.getPath(), lang);
            });
        }

        for (TabBuilder tab : TabBuilder.getAllBuilders()) {
            Map.Entry<String, String> entry = tab.getLangEntry();
            add(entry.getKey(), entry.getValue());
        }
    }

    public void addBlock(Supplier<? extends Block> block) {
        add(block.get(), autoName(block.get()));
    }
    public void addBlock(Supplier<? extends Block> block, String name) {
        add(block.get(), name);
    }

    public void addBlockWithTooltip(Supplier<? extends Block> block, String tooltip) {
        addBlock(block);
        addTooltip(block, tooltip);
    }
    public void addBlockWithTooltip(Supplier<? extends Block> block, String name, String tooltip) {
        addBlock(block, name);
        addTooltip(block, tooltip);
    }

    public void addItem(Supplier<? extends Item> item) {
        add(item.get(), autoName(item.get()));
    }
    public void addItem(Supplier<? extends Item> item, String name) {
        add(item.get(), name);
    }

    public void addItemWithTooltip(Supplier<? extends Item> item, String tooltip) {
        addItem(item);
        addTooltip(item, tooltip);
    }
    public void addItemWithTooltip(Supplier<? extends Item> item, String name, String tooltip) {
        addItem(item, name);
        addTooltip(item, tooltip);
    }

    public void addTooltip(Supplier<? extends ItemLike> item, String tooltip) {
        add(item.get().asItem().getDescriptionId() + ".desc", tooltip);
    }
    public void addTooltip(Supplier<? extends ItemLike> item, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            add(item.get().asItem().getDescriptionId() + ".desc." + i, lines.get(i));
        }
    }

    public void addTab(Supplier<CreativeModeTab> tab, String name) {
        var contents = tab.get().getDisplayName().getContents();
        if (contents instanceof TranslatableContents lang) {
            add(lang.getKey(), name);
        } else {
            System.err.println("Creative tab has non-translatable name: " + tab.get().getDisplayName());
        }
    }

    public void addEntity(Supplier<? extends EntityType<?>> entity) {
        add(entity.get(), autoName(entity.get()));
    }
    public void addEntity(Supplier<? extends EntityType<?>> entity, String name) {
        add(entity.get(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> ench) {
        add(ench.get(), autoName(ench.get()));
    }
    public void addEnchantment(Supplier<? extends Enchantment> ench, String name) {
        add(ench.get(), name);
    }

    public void addEffect(Supplier<? extends MobEffect> effect) {
        add(effect.get(), autoName(effect.get()));
    }
    public void addEffect(Supplier<? extends MobEffect> effect, String name) {
        add(effect.get(), name);
    }

    public void add(Item item, String name) {
        add(item.getDescriptionId(), name);
    }
    public void add(Block block, String name) {
        add(block.getDescriptionId(), name);
    }
    public void add(EntityType<?> entity, String name) {
        add(entity.getDescriptionId(), name);
    }
    public void add(Enchantment ench, String name) {
        Registry<Enchantment> enchantmentRegistry = (Registry<Enchantment>) Registries.ENCHANTMENT;
        ResourceLocation key = enchantmentRegistry.getKey(ench);
        assert key != null;
        add("enchantment." + key.toLanguageKey(), name);
    }
    public void add(MobEffect effect, String name) {
        add(effect.getDescriptionId(), name);
    }
    public void add(ItemStack stack, String name) {
        add(stack.getDescriptionId(), name);
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
        Registry<Enchantment> enchantmentRegistry = (Registry<Enchantment>) Registries.ENCHANTMENT;
        ResourceLocation key = enchantmentRegistry.getKey(ench);
        assert key != null;
        return toEnglishName(key.getPath());
    }
    private String autoName(MobEffect effect) {
        return toEnglishName(Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect)).getPath());
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

    private static String toEnglishName(String id) {
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

    @Override
    public void add(String key, String value) {
        if (!addedKeys.add(key)) {
            System.err.println("Duplicate lang key detected (ignored): " + key);
            return;
        }
        super.add(key, value);
        if (upsideDownLang != null) {
            upsideDownLang.add(key, toUpsideDown(value));
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        var mainRun = super.run(output);
        if (upsideDownLang != null) {
            return CompletableFuture.allOf(mainRun, upsideDownLang.run(output));
        }
        return mainRun;
    }
}