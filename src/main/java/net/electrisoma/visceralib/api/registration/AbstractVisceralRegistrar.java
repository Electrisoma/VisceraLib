package net.electrisoma.visceralib.api.registration;

import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.FluidBuilder;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.electrisoma.visceralib.api.registration.builders.TabBuilder;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractVisceralRegistrar<T extends AbstractVisceralRegistrar<T>> {
    protected final String modId;
    private TabEntry<CreativeModeTab> defaultTabEntry = null;
    private Supplier<TabEntry<CreativeModeTab>> defaultTabEntrySupplier = null;

    protected AbstractVisceralRegistrar(String modId) {
        this.modId = modId;
    }

    public <R> VisceralDeferredRegister<R> deferredRegister(ResourceKey<? extends Registry<R>> registryKey) {
        return VisceralRegistries.getOrCreate(registryKey, modId);
    }

    public T defaultCreativeTab(TabEntry<CreativeModeTab> tabEntry) {
        this.defaultTabEntry = tabEntry;
        this.defaultTabEntrySupplier = null;
        return self();
    }

    public T defaultCreativeTab(Supplier<TabEntry<CreativeModeTab>> tabEntrySupplier) {
        this.defaultTabEntrySupplier = tabEntrySupplier;
        this.defaultTabEntry = null;
        return self();
    }

    public Optional<TabEntry<CreativeModeTab>> getDefaultTabEntry() {
        if (defaultTabEntrySupplier != null) {
            return Optional.ofNullable(defaultTabEntrySupplier.get());
        }
        return Optional.ofNullable(defaultTabEntry);
    }

    public <I extends Item> ItemBuilder<I, T> item(String name, Function<Item.Properties, I> constructor) {
        return new ItemBuilder<>(self(), name, constructor);
    }

    public <B extends Block> BlockBuilder<B, T> block(String name, Function<BlockBehaviour.Properties, B> constructor) {
        return new BlockBuilder<>(self(), name, constructor);
    }

    public FluidBuilder<T> fluid(String name) {
        return new FluidBuilder<>(self(), name);
    }

    public TabBuilder tab(String name) {
        return new TabBuilder(this, name);
    }

    public String getModId() {
        return modId;
    }

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }
}
