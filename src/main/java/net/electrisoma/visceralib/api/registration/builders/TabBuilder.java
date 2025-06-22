package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TabBuilder<R extends AbstractVisceralRegistrar<R>> {
    private static final List<TabBuilder<?>> ALL_BUILDERS = new ArrayList<>();
    private static final int TABS_PER_ROW = 6;
    private static int tabCounter = 0;

    public static List<TabBuilder<?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private final R registrar;
    private final VisceralDeferredRegister<CreativeModeTab> tabRegister;
    private final String name;
    private final String modId;
    private final CreativeModeTab.Row row;
    private final int column;

    private Supplier<ItemStack> iconSupplier = () -> new ItemStack(Items.BARRIER);
    private String displayName = null;
    private Consumer<CreativeModeTab.Builder> builderCustomizer = null;

    private TabEntry<CreativeModeTab> registeredTab = null;
    private VisceralRegistrySupplier<CreativeModeTab> registeredSupplier = null;

    public TabBuilder(R registrar, String name) {
        this.registrar = Objects.requireNonNull(registrar);
        this.name = Objects.requireNonNull(name);
        this.modId = registrar.getModId();
        this.tabRegister = registrar.deferredRegister(Registries.CREATIVE_MODE_TAB);

        this.row = (tabCounter / TABS_PER_ROW == 0) ? CreativeModeTab.Row.TOP : CreativeModeTab.Row.BOTTOM;
        this.column = tabCounter % TABS_PER_ROW;
        tabCounter++;
    }

    public TabBuilder<R> icon(Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = iconSupplier != null ? iconSupplier : () -> new ItemStack(Items.BARRIER);
        return this;
    }

    public TabBuilder<R> icon(ItemStack icon) {
        return icon(() -> icon);
    }

    public TabBuilder<R> icon(net.minecraft.world.item.Item item) {
        return icon(() -> new ItemStack(item));
    }

    public TabBuilder<R> lang(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public TabBuilder<R> customize(Consumer<CreativeModeTab.Builder> customizer) {
        this.builderCustomizer = customizer;
        return this;
    }

    public TabEntry<CreativeModeTab> register() {
        if (registeredTab != null)
            return registeredTab;

        ALL_BUILDERS.add(this);

        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(modId, name));
        VisceralRegistrySupplier<CreativeModeTab> supplier = new VisceralRegistrySupplier<>(key, this::build);

        tabRegister.register(name, this::build);

        registeredTab = new TabEntry<>(supplier);
        return registeredTab;
    }

    private CreativeModeTab build() {
        CreativeModeTab.Builder builder = CreativeModeTab.builder(row, column)
                .icon(iconSupplier)
                .title(Component.translatable("itemGroup." + modId + "." + name));

        if (builderCustomizer != null)
            builderCustomizer.accept(builder);

        return builder.build();
    }

    public String getName() {
        return name;
    }

    public String getModId() {
        return modId;
    }

    public ResourceKey<CreativeModeTab> getKey() {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(modId, name));
    }

    public Optional<VisceralRegistrySupplier<CreativeModeTab>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(displayName);
    }
}
