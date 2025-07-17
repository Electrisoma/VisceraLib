package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

public class TabEntry<T> {
    private VisceralRegistrySupplier<T> supplier;

    public TabEntry(VisceralRegistrySupplier<T> supplier) {
        this.supplier = supplier;
    }

    public void setInternal(VisceralRegistrySupplier<T> newSupplier) {
        if (this.supplier != null)
            throw new IllegalStateException("TabEntry already initialized");
        this.supplier = newSupplier;
    }

    private VisceralRegistrySupplier<T> requireSupplier() {
        if (supplier == null)
            throw new IllegalStateException("TabEntry used before it was registered");
        return supplier;
    }

    public ResourceKey<T> getKey() {
        return requireSupplier().getKey();
    }

    public T get() {
        return requireSupplier().get();
    }

    public void listen(Consumer<T> listener) {
        requireSupplier().listen(listener);
    }
}
