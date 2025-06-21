package net.electrisoma.resotech.api.registration.builders;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.electrisoma.resotech.client.tooltips.ItemDescription;
import net.electrisoma.resotech.client.tooltips.TooltipModifier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.electrisoma.resotech.ResoTech.MOD_ID;

@SuppressWarnings("unused")
public class BlockBuilder {
    private static final List<BlockBuilder> ALL_BUILDERS = new ArrayList<>();
    public static List<BlockBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private final DeferredRegister<Block> blockRegister;
    private final String name;

    private Supplier<Block> blockSupplier;
    private Supplier<BlockBehaviour.Properties> blockPropertiesSupplier = BlockBehaviour.Properties::of;
    private RegistrySupplier<Block> registeredBlock;

    private boolean shouldRegisterBlockItem = false;
    private DeferredRegister<Item> itemRegister;
    private Item.Properties itemProperties = new Item.Properties();

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
    private String langName = null;

    private final List<TagKey<Block>> blockTags = new ArrayList<>();
    public List<TagKey<Block>> getBlockTags() {
        return Collections.unmodifiableList(blockTags);
    }

    private final List<Consumer<Block>> postRegisterTasks = new ArrayList<>();

    public record LootDrop(Item item, int minCount, int maxCount) {}
    private LootDrop lootDrop = null;
    private LootTableProvider lootTableProvider = null;

    @FunctionalInterface
    public interface LootTableProvider {
        void accept(LootTable.Builder lootTableBuilder, Block block);
    }

    public BlockBuilder(DeferredRegister<Block> blockRegister, String name) {
        this.blockRegister = blockRegister;
        this.name = name;
    }

    public BlockBuilder properties(BlockBehaviour.Properties props) {
        this.blockPropertiesSupplier = () -> props;
        return this;
    }
    public BlockBuilder properties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> modifier) {
        Supplier<BlockBehaviour.Properties> previous = this.blockPropertiesSupplier;
        this.blockPropertiesSupplier = () -> modifier.apply(previous.get());
        return this;
    }
    public BlockBuilder initialProperties(Supplier<? extends Block> blockSupplier) {
        this.blockPropertiesSupplier = () -> BlockBehaviour.Properties.ofFullCopy(blockSupplier.get());
        return this;
    }

    public BlockBuilder blockSupplier(Supplier<Block> supplier) {
        this.blockSupplier = supplier;
        return this;
    }
    private Supplier<Block> defaultBlockSupplier() {
        return () -> new Block(blockPropertiesSupplier.get());
    }

    public BlockBuilder withBlockItem(DeferredRegister<Item> itemRegister) {
        this.shouldRegisterBlockItem = true;
        this.itemRegister = itemRegister;
        return this;
    }

    public BlockBuilder itemProperties(Item.Properties props) {
        this.itemProperties = props;
        return this;
    }

    public BlockBuilder tab(ResourceKey<CreativeModeTab> tab) {
        creativeTabs.add(() -> tab);
        return this;
    }
    public BlockBuilder tab(RegistrySupplier<CreativeModeTab> tabSupplier) {
        creativeTabs.add(() -> ResourceKey.create(Registries.CREATIVE_MODE_TAB, tabSupplier.getId()));
        return this;
    }
    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        Set<ResourceKey<CreativeModeTab>> resolved = new HashSet<>();
        for (Supplier<ResourceKey<CreativeModeTab>> supplier : creativeTabs) {
            resolved.add(supplier.get());
        }
        return Collections.unmodifiableSet(resolved);
    }

    public BlockBuilder lang(String langName) {
        this.langName = langName;
        return this;
    }
    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langName);
    }

    public BlockBuilder tag(TagKey<Block> tag) {
        this.blockTags.add(tag);
        return this;
    }
    @SafeVarargs public final BlockBuilder tags(TagKey<Block>... tags) {
        Collections.addAll(this.blockTags, tags);
        return this;
    }

    public BlockBuilder onRegister(Consumer<Block> task) {
        this.postRegisterTasks.add(task);
        return this;
    }
    public BlockBuilder transform(Function<BlockBuilder, BlockBuilder> transformer) {
        return transformer.apply(this);
    }

    public BlockBuilder lootDrop(Item item, int count) {
        return lootDrop(item, count, count);
    }
    public BlockBuilder lootDrop(Item item, int minCount, int maxCount) {
        this.lootDrop = new LootDrop(item, minCount, maxCount);
        return this;
    }
    public Optional<LootDrop> getLootDrop() {
        return Optional.ofNullable(lootDrop);
    }
    public BlockBuilder lootTable(LootTableProvider provider) {
        this.lootTableProvider = provider;
        return this;
    }
    public Optional<LootTableProvider> getLootTableProvider() {
        return Optional.ofNullable(lootTableProvider);
    }

    private RenderType renderLayer;
    public BlockBuilder renderLayer(RenderType type) {
        if (!RenderType.chunkBufferLayers().contains(type)) {
            throw new IllegalArgumentException("RenderType must be one of chunkBufferLayers");
        }
        this.renderLayer = type;
        return this;
    }
    public Optional<RenderType> getRenderLayer() {
        return Optional.ofNullable(renderLayer);
    }

    private boolean hasTooltipLangEntry() {
        String key = "item." + MOD_ID + "." + name + ".tooltip.summary";
        return I18n.exists(key);
    }

    public RegistrySupplier<Block> register() {
        if (blockSupplier == null) blockSupplier = defaultBlockSupplier();
        if (!ALL_BUILDERS.contains(this)) ALL_BUILDERS.add(this);

        registeredBlock = blockRegister.register(name, blockSupplier);

        registeredBlock.listen(block -> {
            postRegisterTasks.forEach(task -> task.accept(block));

            if (shouldRegisterBlockItem && itemRegister != null) {
                RegistrySupplier<Item> blockItemSupplier = itemRegister.register(name, () -> new BlockItem(block, itemProperties));
                blockItemSupplier.listen(item -> {
                    if (hasTooltipLangEntry()) {
                        TooltipModifier.register(item, (stack, tooltip) -> {
                            tooltip.addAll(ItemDescription.fromTranslationKey("item." + MOD_ID + "." + name + ".tooltip").getTooltipLines());
                        });
                    }
                });
            }
        });

        return registeredBlock;
    }
    public RegistrySupplier<Block> getRegisteredBlock() {
        return registeredBlock;
    }
    public String getName() {
        return name;
    }
}