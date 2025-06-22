package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.entry.ItemEntry;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class ItemBuilder<T extends Item, R extends AbstractVisceralRegistrar<R>> {
    private static final List<ItemBuilder<?, ?>> ALL_BUILDERS = new ArrayList<>();
    public static List<ItemBuilder<?, ?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private final R registrar;
    private final VisceralDeferredRegister<Item> itemRegister;
    private final String name;
    private final Function<Item.Properties, T> constructor;

    private Item.Properties properties = new Item.Properties();
    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
    private final List<TagKey<Item>> itemTags = new ArrayList<>();
    private final List<Consumer<T>> postRegisterTasks = new ArrayList<>();

    private String langName = null;
    private Integer burnTime = null;
    private Float compostChance = null;

    public ItemBuilder(R registrar, String name, Function<Item.Properties, T> constructor) {
        this.registrar = Objects.requireNonNull(registrar);
        this.name = Objects.requireNonNull(name);
        this.itemRegister = registrar.deferredRegister(Registries.ITEM);
        this.constructor = constructor;
    }

    public ItemBuilder<T, R> properties(Function<Item.Properties, Item.Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return this;
    }

    public ItemBuilder<T, R> tab(Supplier<ResourceKey<CreativeModeTab>> tabKeySupplier) {
        creativeTabs.add(tabKeySupplier);
        return this;
    }

    public ItemBuilder<T, R> tab(Holder.Reference<CreativeModeTab> tabHolder) {
        creativeTabs.add(tabHolder::key);
        return this;
    }

    public ItemBuilder<T, R> tag(TagKey<Item> tag) {
        itemTags.add(tag);
        return this;
    }

    @SafeVarargs
    public final ItemBuilder<T, R> tags(TagKey<Item>... tags) {
        Collections.addAll(itemTags, tags);
        return this;
    }

    public ItemBuilder<T, R> lang(String langName) {
        this.langName = langName;
        return this;
    }

    public ItemBuilder<T, R> burnTime(int ticks) {
        this.burnTime = ticks;
        return this;
    }

    public ItemBuilder<T, R> compostChance(float chance) {
        this.compostChance = chance;
        return this;
    }

    public ItemBuilder<T, R> onRegister(Consumer<T> consumer) {
        this.postRegisterTasks.add(consumer);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ItemEntry<T> register() {
        if (creativeTabs.isEmpty())
            registrar.getDefaultTabEntry().ifPresent(entry -> creativeTabs.add(entry::getKey));

        if (!ALL_BUILDERS.contains(this)) ALL_BUILDERS.add(this);

        VisceralRegistrySupplier<Item> rawRegistered = itemRegister.register(name, () -> constructor.apply(properties));

        ResourceKey<T> castKey = (ResourceKey<T>) rawRegistered.getKey();

        VisceralRegistrySupplier<T> typedRegistered = new VisceralRegistrySupplier<>(
                castKey,
                () -> (T) rawRegistered.get()
        );

        typedRegistered.listen(item -> {
            for (Consumer<T> task : postRegisterTasks)
                task.accept(item);
        });

        return new ItemEntry<>(typedRegistered);
    }

    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return creativeTabs.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }

    public List<TagKey<Item>> getTags() {
        return List.copyOf(itemTags);
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langName);
    }

    public Optional<Integer> getBurnTime() {
        return Optional.ofNullable(burnTime);
    }

    public Optional<Float> getCompostChance() {
        return Optional.ofNullable(compostChance);
    }

    public String getName() {
        return name;
    }

    public R getRegistrar() {
        return registrar;
    }
}
