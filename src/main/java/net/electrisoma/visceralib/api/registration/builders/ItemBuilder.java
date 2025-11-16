package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.entry.TabEntry;
import net.electrisoma.visceralib.api.registration.helpers.CreativeTabBuilderRegistry;
import net.electrisoma.visceralib.api.registration.entry.ItemEntry;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;

import net.electrisoma.visceralib.api.registration.helpers.ICreativeTabOutputs;
import net.electrisoma.visceralib.data.providers.VisceralLangProvider;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class ItemBuilder<T extends Item, R extends AbstractVisceralRegistrar<R>>
        extends AbstractBuilder<T, R, ItemBuilder<T, R>>
        implements ICreativeTabOutputs {

    private static final List<ItemBuilder<?, ?>> ALL_BUILDERS = new ArrayList<>();

    static {
        CreativeTabBuilderRegistry.registerBuilderProvider(ItemBuilder::getAllBuilders);
    }

    private final VisceralDeferredRegister<Item> register;
    private final Function<Item.Properties, T> constructor;

    private Item.Properties properties = new Item.Properties();
    private VisceralRegistrySupplier<T> registeredSupplier;
    private final List<TagKey<Item>> tags = new ArrayList<>();
    private Supplier<Supplier<? extends ItemRenderer>> customRendererFactory;
    private ResourceLocation customParentModel;

    private Integer burnTime;
    private Float compostChance;

    public ItemBuilder(R registrar, String name, Function<Item.Properties, T> constructor) {
        super(registrar, name);
        this.constructor = constructor;
        this.register = registrar.deferredRegister(Registries.ITEM);
        ALL_BUILDERS.add(this);
    }

    public ItemBuilder<T, R> properties(Function<Item.Properties, Item.Properties> modifier) {
        this.properties = modifier.apply(this.properties);
        return self();
    }

    public ItemBuilder<T, R> burnTime(int ticks) {
        this.burnTime = ticks;
        return self();
    }

    public ItemBuilder<T, R> compostChance(float chance) {
        this.compostChance = chance;
        return self();
    }

    public ItemBuilder<T, R> setCustomRendererFactory(Supplier<Supplier<? extends ItemRenderer>> factory) {
        this.customRendererFactory = factory;
        return self();
    }

    public ItemBuilder<T, R> model(ResourceLocation parent) {
        this.customParentModel = parent;
        return self();
    }

    @SuppressWarnings("unchecked")
    public ItemEntry<T> register() {
        if (isNoTab())
            creativeTabs.clear();
        else if (creativeTabs.isEmpty()) {
            Optional<TabEntry<CreativeModeTab>> maybeTab = AbstractVisceralRegistrar.getThreadTab();
            if (maybeTab.isPresent())
                creativeTabs.add(maybeTab.get()::getKey);
            else registrar.getDefaultTabEntry().ifPresent(entry -> creativeTabs.add(entry::getKey));
        }

        VisceralRegistrySupplier<Item> raw =
                register.register(name, () -> constructor.apply(properties));

        VisceralRegistrySupplier<T> typed = new VisceralRegistrySupplier<>(
                (ResourceKey<T>) raw.getKey(),
                () -> (T) raw.get()
        );

        typed.listen(item -> postRegisterTasks.forEach(task -> task.accept(item)));
        this.registeredSupplier = typed;

        return new ItemEntry<>(typed);
    }

    public ItemBuilder<T, R> tag(TagKey<Item> tag) {
        tags.add(tag);
        return self();
    }

    @SafeVarargs
    public final ItemBuilder<T, R> tags(TagKey<Item>... tags) {
        Collections.addAll(this.tags, tags);
        return self();
    }

    public List<TagKey<Item>> getTags() {
        return List.copyOf(tags);
    }

    public Optional<Supplier<? extends ItemRenderer>> getCustomRendererFactory() {
        return Optional.ofNullable(customRendererFactory).map(Supplier::get);
    }

    public Optional<Integer> getBurnTime() {
        return Optional.ofNullable(burnTime);
    }

    public Optional<Float> getCompostChance() {
        return Optional.ofNullable(compostChance);
    }

    public Optional<VisceralRegistrySupplier<T>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }

    @Override
    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return super.creativeTabs.stream().map(Supplier::get).collect(Collectors.toSet());
    }

    @Override
    public Collection<ItemStack> getTabContents() {
        return getRegisteredSupplier()
                .map(supplier -> List.of(new ItemStack(supplier.get())))
                .orElse(Collections.emptyList());
    }

    public static List<ItemBuilder<?, ?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    public Optional<ResourceLocation> getCustomParentModel() {
        return Optional.ofNullable(customParentModel);
    }

    public static void provideLang(VisceralLangProvider provider) {
        for (ItemBuilder<?, ?> builder : getAllBuilders()) {
            builder.getRegisteredSupplier().ifPresent(supplier -> {
                Item item = supplier.get();
                var id = BuiltInRegistries.ITEM.getKey(item);

                if (!id.getNamespace().equals(provider.getModId()))
                    return;

                String langKey = "item." + id.getNamespace() + "." + id.getPath();
                String langValue = builder.getLangEntry().orElse(VisceralLangProvider.toEnglishName(id.getPath()));

                provider.add(langKey, langValue);
            });
        }
    }
}
