package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class BlockBuilder<T extends Block, R extends AbstractVisceralRegistrar<R>> {
    private static final List<BlockBuilder<?, ?>> ALL_BUILDERS = new ArrayList<>();

    public static List<BlockBuilder<?, ?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private final R registrar;
    private final VisceralDeferredRegister<Block> blockRegister;
    private final String name;
    private Supplier<T> blockSupplier;
    private BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
    private VisceralRegistrySupplier<T> registeredSupplier;

    private final Function<BlockBehaviour.Properties, T> constructor;
    private boolean shouldRegisterBlockItem = true; // Default to true
    private VisceralDeferredRegister<Item> itemRegister;
    private Item.Properties itemProperties = new Item.Properties();
    private Function<T, BlockItem> blockItemFactory = block -> new BlockItem(block, itemProperties);

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
    private final List<TagKey<Block>> blockTags = new ArrayList<>();
    private String langName = null;
    private RenderType renderLayer = null;

    private final List<Consumer<T>> postRegisterTasks = new ArrayList<>();

    public record LootDrop(Item item, int minCount, int maxCount) {}

    @FunctionalInterface
    public interface LootTableProvider {
        void accept(LootTable.Builder lootTableBuilder, Block block);
    }

    private LootDrop lootDrop = null;
    private LootTableProvider lootTableProvider = null;

    public BlockBuilder(R registrar, String name, Function<BlockBehaviour.Properties, T> constructor) {
        this.registrar = Objects.requireNonNull(registrar);
        this.name = Objects.requireNonNull(name);
        this.blockRegister = registrar.deferredRegister(Registries.BLOCK);
        this.constructor = Objects.requireNonNull(constructor);
    }

    public BlockBuilder<T, R> blockSupplier(Supplier<T> supplier) {
        this.blockSupplier = Objects.requireNonNull(supplier);
        return this;
    }

    private Supplier<T> defaultBlockSupplier() {
        return () -> constructor.apply(properties);
    }

    public BlockBuilder<T, R> properties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return this;
    }

    public BlockBuilder<T, R> tab(Supplier<ResourceKey<CreativeModeTab>> tabSupplier) {
        creativeTabs.add(tabSupplier);
        return this;
    }

    public BlockBuilder<T, R> tab(Holder.Reference<CreativeModeTab> tabHolder) {
        creativeTabs.add(tabHolder::key);
        return this;
    }

    public BlockBuilder<T, R> tag(TagKey<Block> tag) {
        blockTags.add(tag);
        return this;
    }

    @SafeVarargs
    public final BlockBuilder<T, R> tags(TagKey<Block>... tags) {
        Collections.addAll(blockTags, tags);
        return this;
    }

    public BlockBuilder<T, R> onRegister(Consumer<T> task) {
        postRegisterTasks.add(task);
        return this;
    }

    public BlockBuilder<T, R> withBlockItem(VisceralDeferredRegister<Item> itemRegister) {
        this.shouldRegisterBlockItem = true;
        this.itemRegister = itemRegister;
        return this;
    }

    public BlockBuilder<T, R> noBlockItem() {
        this.shouldRegisterBlockItem = false;
        this.itemRegister = null;
        return this;
    }

    public BlockBuilder<T, R> itemProperties(Item.Properties props) {
        this.itemProperties = Objects.requireNonNull(props);
        this.blockItemFactory = block -> new BlockItem(block, props);
        return this;
    }

    public BlockBuilder<T, R> blockItemFactory(Function<T, BlockItem> factory) {
        this.blockItemFactory = Objects.requireNonNull(factory);
        return this;
    }

    public BlockBuilder<T, R> lang(String langName) {
        this.langName = langName;
        return this;
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langName);
    }

    public BlockBuilder<T, R> renderLayer(RenderType renderLayer) {
        this.renderLayer = renderLayer;
        return this;
    }

    public Optional<RenderType> getRenderLayer() {
        return Optional.ofNullable(renderLayer);
    }

    public BlockBuilder<T, R> lootDrop(Item item, int count) {
        return lootDrop(item, count, count);
    }

    public BlockBuilder<T, R> lootDrop(Item item, int minCount, int maxCount) {
        this.lootDrop = new LootDrop(item, minCount, maxCount);
        return this;
    }

    public Optional<LootDrop> getLootDrop() {
        return Optional.ofNullable(lootDrop);
    }

    public BlockBuilder<T, R> lootTable(LootTableProvider provider) {
        this.lootTableProvider = provider;
        return this;
    }

    public Optional<LootTableProvider> getLootTableProvider() {
        return Optional.ofNullable(lootTableProvider);
    }

    public BlockBuilder<T, R> transform(Function<BlockBuilder<T, R>, BlockBuilder<T, R>> transformer) {
        return transformer.apply(this);
    }

    @SuppressWarnings("unchecked")
    public BlockEntry<T> register() {
        if (blockSupplier == null) blockSupplier = defaultBlockSupplier();

        if (creativeTabs.isEmpty())
            registrar.getDefaultTabEntry().ifPresent(entry -> creativeTabs.add(entry::getKey));

        if (!ALL_BUILDERS.contains(this)) ALL_BUILDERS.add(this);

        VisceralRegistrySupplier<Block> rawRegistered = blockRegister.register(name, () -> blockSupplier.get());
        VisceralRegistrySupplier<T> typedRegistered = getVisceralRegistrySupplier(rawRegistered);

        registeredSupplier = typedRegistered;

        if (shouldRegisterBlockItem && itemRegister == null) {
            itemRegister = registrar.deferredRegister(Registries.ITEM);
        }

        if (shouldRegisterBlockItem && itemRegister != null) {
            itemRegister.register(name, () -> blockItemFactory.apply(typedRegistered.get()));
        }

        return new BlockEntry<>(typedRegistered);
    }

    private @NotNull VisceralRegistrySupplier<T> getVisceralRegistrySupplier(VisceralRegistrySupplier<Block> rawRegistered) {
        ResourceKey<T> castKey = (ResourceKey<T>) rawRegistered.getKey();

        VisceralRegistrySupplier<T> typedRegistered = new VisceralRegistrySupplier<>(
                castKey,
                () -> (T) rawRegistered.get()
        );

        typedRegistered.listen(block -> {
            for (Consumer<T> task : postRegisterTasks)
                task.accept(block);
        });

        return typedRegistered;
    }

    public Optional<VisceralRegistrySupplier<T>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }

    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return creativeTabs.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }

    public List<TagKey<Block>> getBlockTags() {
        return List.copyOf(blockTags);
    }

    public String getName() {
        return name;
    }

    public R getRegistrar() {
        return registrar;
    }
}
