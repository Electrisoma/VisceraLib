package net.electrisoma.resotech.registry.helpers;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ItemBuilder {
    private static final List<ItemBuilder> ALL_BUILDERS = new ArrayList<>();

    private final DeferredRegister<Item> deferredRegister;
    private final String name;

    private Supplier<Item> itemSupplier;
    private Item.Properties properties = new Item.Properties();

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
    private Integer burnTime = null;
    private Float compostChance = null;
    private String langName = null;

    private final List<Consumer<Item>> postRegisterTasks = new ArrayList<>();

    public ItemBuilder(DeferredRegister<Item> deferredRegister, String name) {
        this.deferredRegister = deferredRegister;
        this.name = name;
    }

    public ItemBuilder properties(Item.Properties props) {
        this.properties = props;
        return this;
    }

    public ItemBuilder modifyProperties(Function<Item.Properties, Item.Properties> modifier) {
        this.properties = modifier.apply(properties);
        return this;
    }

    public ItemBuilder itemSupplier(Supplier<Item> supplier) {
        this.itemSupplier = supplier;
        return this;
    }

    /**
     * Add a creative tab using a direct ResourceKey supplier (manual).
     */
    public ItemBuilder tab(Supplier<ResourceKey<CreativeModeTab>> tabKeySupplier) {
        creativeTabs.add(tabKeySupplier);
        return this;
    }
    public ItemBuilder tab(RegistrySupplier<CreativeModeTab> tabSupplier) {
        Objects.requireNonNull(tabSupplier, "Creative tab supplier cannot be null");
        creativeTabs.add(() -> {
            var id = tabSupplier.getId();
            if (id == null) {
                throw new IllegalStateException("Creative tab ID is null. Is it being registered too late?");
            }
            return ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        });
        return this;
    }
    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return creativeTabs.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }

    public ItemBuilder lang(String langName) {
        this.langName = langName;
        return this;
    }
    public String getName() {
        return name;
    }
    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langName);
    }

    public ItemBuilder burnTime(int ticks) {
        this.burnTime = ticks;
        return this;
    }

    public ItemBuilder compostChance(float chance) {
        this.compostChance = chance;
        return this;
    }

    public ItemBuilder onRegister(Consumer<Item> consumer) {
        this.postRegisterTasks.add(consumer);
        return this;
    }

    private Supplier<Item> defaultSupplier() {
        return () -> new Item(properties);
    }

    public RegistrySupplier<Item> register() {
        if (itemSupplier == null) itemSupplier = defaultSupplier();

        ALL_BUILDERS.add(this);

        RegistrySupplier<Item> registered = deferredRegister.register(name, itemSupplier);

        registered.listen(item -> {
            Set<ResourceKey<CreativeModeTab>> resolvedTabs = creativeTabs.stream()
                    .map(Supplier::get)
                    .collect(Collectors.toUnmodifiableSet());

            // Future hooks
            // ItemBuilderHooks.addToCreativeTabs(item, resolvedTabs);
            // if (burnTime != null) ItemBuilderHooks.addFuel(item, burnTime);
            // if (compostChance != null) ItemBuilderHooks.addCompostable(item, compostChance);

            for (Consumer<Item> task : postRegisterTasks) {
                task.accept(item);
            }
        });

        return registered;
    }

    public static List<ItemBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }
}
