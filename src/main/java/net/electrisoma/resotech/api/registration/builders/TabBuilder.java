package net.electrisoma.resotech.api.registration.builders;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.registry.ResoTechTabs;

import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class TabBuilder {
    private static final List<TabBuilder> ALL_BUILDERS = new ArrayList<>();
    public static List<TabBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private static int tabCounter = 0;
    private static final int TABS_PER_ROW = 6;

    private final String modId;
    private final String name;
    private Supplier<ItemStack> iconSupplier;
    private String displayName;
    private Consumer<CreativeModeTab.Builder> builderCustomizer;

    private final CreativeModeTab.Row row;
    private final int column;

    private Supplier<Collection<? extends ItemLike>> explicitContentSupplier = Collections::emptyList;
    private Predicate<Item> itemFilter = item -> true;

    public TabBuilder(String modId, String name) {
        this.modId = modId;
        this.name = name;

        this.row = (tabCounter / TABS_PER_ROW == 0) ? CreativeModeTab.Row.TOP : CreativeModeTab.Row.BOTTOM;
        this.column = tabCounter % TABS_PER_ROW;
        tabCounter++;
    }

    public TabBuilder icon(Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = iconSupplier;
        return this;
    }
    public TabBuilder icon(Item item) {
        return icon(() -> new ItemStack(item));
    }
    public TabBuilder icon(Block block) {
        return icon(() -> new ItemStack(block.asItem()));
    }
    public TabBuilder icon(RegistrySupplier<?> supplier) {
        return icon(() -> {
            Object obj = supplier.get();
            if (obj instanceof ItemLike itemLike) {
                return new ItemStack(itemLike.asItem());
            } else if (obj == null) {
                return new ItemStack(Items.BARRIER);
            } else {
                throw new IllegalArgumentException("Unsupported icon supplier type: " + obj.getClass().getName());
            }
        });
    }

    public TabBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
    public Map.Entry<String, String> getLangEntry() {
        String key = "itemGroup." + modId + "." + name;
        String value = displayName != null ? displayName : name;
        return Map.entry(key, value);
    }

    public TabBuilder customize(Consumer<CreativeModeTab.Builder> customizer) {
        this.builderCustomizer = customizer;
        return this;
    }

    public TabBuilder explicitContent(Supplier<Collection<? extends ItemLike>> supplier) {
        this.explicitContentSupplier = supplier != null ? supplier : Collections::emptyList;
        return this;
    }
    public TabBuilder itemFilter(Predicate<Item> itemFilter) {
        this.itemFilter = itemFilter != null ? itemFilter : item -> true;
        return this;
    }

    public RegistrySupplier<CreativeModeTab> register() {
        ALL_BUILDERS.add(this);
        return ResoTechTabs.CREATIVE_TABS.register(name, this::build);
    }
    public CreativeModeTab build() {
        CreativeModeTab.Builder builder = CreativeModeTab.builder(row, column);
        builder.icon(() -> iconSupplier != null ? iconSupplier.get() : new ItemStack(Items.BARRIER));
        builder.title(Component.translatable("itemGroup." + modId + "." + name));

        ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, getKey());

        builder.displayItems((parameters, output) -> {
            Set<Item> includedItems = new HashSet<>();

            for (ItemLike itemLike : explicitContentSupplier.get()) {
                Item item = itemLike.asItem();
                if (itemFilter.test(item)) includedItems.add(item);
            }

            for (ItemBuilder itemBuilder : ItemBuilder.getAllBuilders()) {
                if (itemBuilder.getTabs().contains(tabKey)) {
                    Item item = BuiltInRegistries.ITEM.get(ResoTech.path(itemBuilder.getName()));
                    if (itemFilter.test(item)) includedItems.add(item);
                }
            }

            for (BlockBuilder blockBuilder : BlockBuilder.getAllBuilders()) {
                if (blockBuilder.getTabs().contains(tabKey)) {
                    Item item = BuiltInRegistries.ITEM.get(ResoTech.path(blockBuilder.getName()));
                    if (itemFilter.test(item)) includedItems.add(item);
                }
            }

            includedItems.stream()
                    .sorted(Comparator.comparing(i -> BuiltInRegistries.ITEM.getKey(i).toString()))
                    .forEach(item ->
                            output.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
        });

        if (builderCustomizer != null) builderCustomizer.accept(builder);

        return builder.build();
    }

    public ResourceKey<CreativeModeTab> getResourceKey() {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, getKey());
    }
    public ResourceLocation getKey() {
        return ResoTech.path(name);
    }
}
