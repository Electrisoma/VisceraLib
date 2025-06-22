package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class TabEntry<T> {
    private final VisceralRegistrySupplier<T> supplier;

    public TabEntry(VisceralRegistrySupplier<T> supplier) {
        this.supplier = supplier;
    }

    public ResourceKey<T> getKey() {
        return supplier.getKey();
    }

    public T get() {
        return supplier.get();
    }
}
