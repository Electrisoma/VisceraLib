package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractBuilder<T, R extends AbstractVisceralRegistrar<R>, S extends AbstractBuilder<T, R, S>> {
    protected final R registrar;
    protected final String name;
    protected String langName;

    protected final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
    protected final List<Consumer<T>> postRegisterTasks = new ArrayList<>();

    public abstract Optional<VisceralRegistrySupplier<T>> getRegisteredSupplier();

    protected AbstractBuilder(R registrar, String name) {
        this.registrar = Objects.requireNonNull(registrar);
        this.name = Objects.requireNonNull(name);
    }

    public S lang(String langName) {
        this.langName = langName;
        return self();
    }

    public S tab(Supplier<ResourceKey<CreativeModeTab>> tabKeySupplier) {
        creativeTabs.add(tabKeySupplier);
        return self();
    }

    public S tab(Holder.Reference<CreativeModeTab> tabHolder) {
        return tab(tabHolder::key);
    }

    public S onRegister(Consumer<T> consumer) {
        postRegisterTasks.add(consumer);
        return self();
    }

    @SuppressWarnings("unchecked")
    public S transform(Function<S, S> fn) {
        return fn.apply(self());
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langName);
    }

    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return creativeTabs.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }

    public String getName() {
        return name;
    }

    public R getRegistrar() {
        return registrar;
    }

    @SuppressWarnings("unchecked")
    protected S self() {
        return (S) this;
    }
}
