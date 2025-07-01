package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.world.item.Item;

public record ItemEntry<T extends Item>(VisceralRegistrySupplier<T> supplier) {
    public T get() {
        return supplier.get();
    }
}
