package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.helpers.CreativeTabBuilderRegistry;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.BlockEntry;
import net.electrisoma.visceralib.api.registration.helpers.ICreativeTabOutputs;
import net.electrisoma.visceralib.api.registration.helpers.TaggableBuilder;

import net.electrisoma.visceralib.data.providers.VisceralLangProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class BlockBuilder<T extends Block, R extends AbstractVisceralRegistrar<R>>
        extends AbstractBuilder<T, R, BlockBuilder<T, R>>
        implements TaggableBuilder<Block>, ICreativeTabOutputs {

    private static final List<BlockBuilder<?, ?>> ALL_BUILDERS = new ArrayList<>();

    static {
        CreativeTabBuilderRegistry.registerBuilderProvider(BlockBuilder::getAllBuilders);
    }

    private final VisceralDeferredRegister<Block> blockRegister;
    private final Function<BlockBehaviour.Properties, T> constructor;

    private Supplier<T> blockSupplier;
    private BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
    private VisceralRegistrySupplier<T> registeredSupplier;

    private boolean shouldRegisterBlockItem = true;
    private VisceralDeferredRegister<Item> itemRegister;
    private Item.Properties itemProperties = new Item.Properties();
    private Function<T, BlockItem> blockItemFactory = block -> new BlockItem(block, itemProperties);

    private VisceralRegistrySupplier<Item> blockItemSupplier; // <-- track BlockItem supplier here

    private final List<TagKey<Block>> tags = new ArrayList<>();
    private RenderType renderLayer;
    private LootDrop lootDrop;
    private LootTableProvider lootTableProvider;

    public record LootDrop(Item item, int minCount, int maxCount) {}
    @FunctionalInterface public interface LootTableProvider {
        void accept(LootTable.Builder builder, Block block);
    }

    public BlockBuilder(R registrar, String name, Function<BlockBehaviour.Properties, T> constructor) {
        super(registrar, name);
        this.constructor = constructor;
        this.blockRegister = registrar.deferredRegister(Registries.BLOCK);
        ALL_BUILDERS.add(this);
    }

    public void blockSupplier(Supplier<T> supplier) {
        this.blockSupplier = supplier;
    }

    public BlockBuilder<T, R> initialProperties(Block base) {
        this.properties = BlockBehaviour.Properties.ofFullCopy(base);
        return this;
    }

    public BlockBuilder<T, R> properties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return this;
    }

    public BlockBuilder<T, R> withBlockItem(VisceralDeferredRegister<Item> register) {
        this.shouldRegisterBlockItem = true;
        this.itemRegister = register;
        return this;
    }

    public BlockBuilder<T, R> noBlockItem() {
        this.shouldRegisterBlockItem = false;
        return this;
    }

    public BlockBuilder<T, R> itemProperties(Item.Properties props) {
        this.itemProperties = props;
        this.blockItemFactory = block -> new BlockItem(block, props);
        return this;
    }

    public BlockBuilder<T, R> blockItemFactory(Function<T, BlockItem> factory) {
        this.blockItemFactory = factory;
        return this;
    }

    public BlockBuilder<T, R> renderLayer(RenderType layer) {
        this.renderLayer = layer;
        return this;
    }

    public BlockBuilder<T, R> lootDrop(Item item, int min, int max) {
        this.lootDrop = new LootDrop(item, min, max);
        return this;
    }

    public BlockBuilder<T, R> lootDrop(Item item, int count) {
        return lootDrop(item, count, count);
    }

    public BlockBuilder<T, R> lootTable(LootTableProvider provider) {
        this.lootTableProvider = provider;
        return this;
    }

    @SuppressWarnings("unchecked")
    public BlockEntry<T> register() {
        if (blockSupplier == null)
            blockSupplier = () -> constructor.apply(properties);

        if (creativeTabs.isEmpty())
            registrar.getDefaultTabEntry().ifPresent(entry -> creativeTabs.add(entry::getKey));

        VisceralRegistrySupplier<Block> raw = blockRegister.register(name, () -> blockSupplier.get());

        VisceralRegistrySupplier<T> typed = new VisceralRegistrySupplier<>(
                (ResourceKey<T>) raw.getKey(),
                () -> (T) raw.get()
        );

        typed.listen(block -> postRegisterTasks.forEach(task -> task.accept(block)));
        this.registeredSupplier = typed;

        if (shouldRegisterBlockItem) {
            if (itemRegister == null) itemRegister = registrar.deferredRegister(Registries.ITEM);
            blockItemSupplier = itemRegister.register(name, () -> blockItemFactory.apply(typed.get()));
        }

        return new BlockEntry<>(typed);
    }

    @Override
    public BlockBuilder<T, R> tag(TagKey<Block> tag) {
        tags.add(tag);
        return self();
    }

    @Override
    @SafeVarargs
    public final BlockBuilder<T, R> tags(TagKey<Block>... tags) {
        Collections.addAll(this.tags, tags);
        return self();
    }

    @Override
    public List<TagKey<Block>> getTags() {
        return List.copyOf(tags);
    }

    public Optional<RenderType> getRenderLayer() {
        return Optional.ofNullable(renderLayer);
    }

    public Optional<LootDrop> getLootDrop() {
        return Optional.ofNullable(lootDrop);
    }

    public Optional<LootTableProvider> getLootTableProvider() {
        return Optional.ofNullable(lootTableProvider);
    }

    public Optional<VisceralRegistrySupplier<T>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }

    public BlockBehaviour.Properties getProperties() {
        return properties;
    }

    public static List<BlockBuilder<?, ?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    @Override
    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return super.creativeTabs.stream().map(Supplier::get).collect(Collectors.toSet());
    }

    @Override
    public Collection<ItemStack> getTabContents() {
        return Optional.ofNullable(blockItemSupplier)
                .map(supplier -> List.of(new ItemStack(supplier.get())))
                .orElse(Collections.emptyList());
    }

    public static void provideLang(VisceralLangProvider provider) {
        for (BlockBuilder<?, ?> builder : getAllBuilders()) {
            builder.getRegisteredSupplier().ifPresent(supplier -> {
                Block block = supplier.get();
                var id = BuiltInRegistries.BLOCK.getKey(block);

                if (!id.getNamespace().equals(provider.getModId()))
                    return;

                String langKey = "block." + id.getNamespace() + "." + id.getPath();
                String langValue = builder.getLangEntry().orElse(VisceralLangProvider.toEnglishName(id.getPath()));

                provider.add(langKey, langValue);
            });
        }
    }
}
