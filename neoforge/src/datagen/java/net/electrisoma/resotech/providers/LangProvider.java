package net.electrisoma.resotech.providers;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.BlockBuilder;
import net.electrisoma.resotech.api.registration.FluidBuilder;
import net.electrisoma.resotech.api.registration.ItemBuilder;
import net.electrisoma.resotech.api.registration.TabBuilder;
import net.electrisoma.resotech.registry.ResoTechAdvancements;
import net.electrisoma.resotech.registry.ResoTechBlocks;
import net.electrisoma.resotech.registry.ResoTechItems;
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
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LangProvider extends LanguageProvider {
    private final LangProvider upsideDownLang;
    private final Set<String> addedKeys = new HashSet<>();

    public LangProvider(PackOutput output, String modid) {
        super(output, modid, "en_us");
        this.upsideDownLang = new LangProvider(output, "en_ud");
    }
    private LangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.upsideDownLang = null;
    }

    @Override
    protected void addTranslations() {
        for (Block block : ResoTechBlocks.BLOCKS.getRegistrar()) {
            var id = BuiltInRegistries.BLOCK.getKey(block);
            if (ResoTech.MOD_ID.equals(id.getNamespace())) {
                boolean hasLang = BlockBuilder.getAllBuilders().stream()
                        .anyMatch(builder -> builder.getName().equals(id.getPath()) && builder.getLangEntry().isPresent());
                if (!hasLang) addBlock(() -> block);
            }
        }

        for (var builder : BlockBuilder.getAllBuilders()) {
            builder.getLangEntry().ifPresent(lang ->
                    add("block." + ResoTech.MOD_ID + "." + builder.getName(), lang));
        }

        for (Item item : ResoTechItems.ITEMS.getRegistrar()) {
            var id = BuiltInRegistries.ITEM.getKey(item);
            if (ResoTech.MOD_ID.equals(id.getNamespace())) {
                boolean hasLang = ItemBuilder.getAllBuilders().stream()
                        .anyMatch(builder -> builder.getName().equals(id.getPath()) && builder.getLangEntry().isPresent());
                if (!hasLang) addItem(() -> item);
            }
        }

        for (var builder : ItemBuilder.getAllBuilders()) {
            builder.getLangEntry().ifPresent(lang ->
                    add("item." + ResoTech.MOD_ID + "." + builder.getName(), lang));
        }

        for (TabBuilder tab : TabBuilder.getAllBuilders()) {
            var entry = tab.getLangEntry();
            add(entry.getKey(), entry.getValue());
        }

        for (FluidBuilder fluid : FluidBuilder.getAllBuilders()) {
            fluid.getLangEntry().ifPresent(lang -> {
                String name = fluid.getName();
                add("block." + ResoTech.MOD_ID + "." + name, lang);
                add("item." + ResoTech.MOD_ID + "." + name + "_bucket", lang + " Bucket");
            });
        }

        ResoTechAdvancements.provideLang(this::add);
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
        var main = super.run(output);
        if (upsideDownLang != null) {
            return CompletableFuture.allOf(main, upsideDownLang.run(output));
        }
        return main;
    }

    @Override
    public String getName() {
        return ResoTech.NAME + " Lang";
    }

    public void addBlock(Supplier<? extends Block> block) {
        add(block.get(), autoName(block.get()));
    }
    public void addBlock(Supplier<? extends Block> block, String name) {
        add(block.get(), name);
    }

    public void addItem(Supplier<? extends Item> item) {
        add(item.get(), autoName(item.get()));
    }
    public void addItem(Supplier<? extends Item> item, String name) {
        add(item.get(), name);
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
        if (contents instanceof TranslatableContents translatable) {
            add(translatable.getKey(), name);
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
        ResourceLocation key = Registries.ENCHANTMENT.registry();
        add("enchantment." + key.getNamespace() + "." + key.getPath(), name);
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
        ResourceLocation key = Registries.ENCHANTMENT.registry();
        return toEnglishName(key.getPath());
    }
    private String autoName(MobEffect effect) {
        return toEnglishName(BuiltInRegistries.MOB_EFFECT.getKey(effect).getPath());
    }

    private static String toEnglishName(String id) {
        return Arrays.stream(id.split("_"))
                .map(s -> s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1))
                .collect(Collectors.joining(" "));
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

    private String toUpsideDown(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            int index = NORMAL.indexOf(c);
            result.append(index >= 0 ? UPSIDE.charAt(index) : c);
        }
        return result.toString();
    }
}
