package net.electrisoma.resotech.registry.helpers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockBuilder {
    private static final List<BlockBuilder> ALL_BUILDERS = new ArrayList<>();

    private final DeferredRegister<Block> blockRegister;
    private final String name;

    private Supplier<Block> blockSupplier;
    private BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of();

    private boolean shouldRegisterBlockItem = false;
    private DeferredRegister<Item> itemRegister;
    private Item.Properties itemProperties = new Item.Properties();

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
    private String langName = null;
    private final List<Consumer<Block>> postRegisterTasks = new ArrayList<>();

    public BlockBuilder(DeferredRegister<Block> blockRegister, String name) {
        this.blockRegister = blockRegister;
        this.name = name;
    }

    public BlockBuilder properties(BlockBehaviour.Properties props) {
        this.blockProperties = props;
        return this;
    }

    public BlockBuilder modifyProperties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> modifier) {
        this.blockProperties = modifier.apply(blockProperties);
        return this;
    }

    public BlockBuilder blockSupplier(Supplier<Block> supplier) {
        this.blockSupplier = supplier;
        return this;
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
        Set<ResourceKey<CreativeModeTab>> resolvedTabs = new HashSet<>();
        for (Supplier<ResourceKey<CreativeModeTab>> sup : creativeTabs) {
            resolvedTabs.add(sup.get());
        }
        return Collections.unmodifiableSet(resolvedTabs);
    }

    public BlockBuilder lang(String langName) {
        this.langName = langName;
        return this;
    }
    public String getName() {
        return name;
    }
    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langName);
    }

    public BlockBuilder onRegister(Consumer<Block> task) {
        this.postRegisterTasks.add(task);
        return this;
    }

    private Supplier<Block> defaultBlockSupplier() {
        return () -> new Block(blockProperties);
    }

    public RegistrySupplier<Block> register() {
        if (blockSupplier == null) blockSupplier = defaultBlockSupplier();

        ALL_BUILDERS.add(this);

        RegistrySupplier<Block> blockEntry = blockRegister.register(name, blockSupplier);

        blockEntry.listen(block -> {
            postRegisterTasks.forEach(task -> task.accept(block));

            if (shouldRegisterBlockItem && itemRegister != null) {
                itemRegister.register(name, () -> new BlockItem(block, itemProperties));
            }
        });

        return blockEntry;
    }
    public static List<BlockBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }
}
