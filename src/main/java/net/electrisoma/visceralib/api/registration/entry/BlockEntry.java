package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.world.level.block.Block;

public record BlockEntry<T extends Block>(VisceralRegistrySupplier<T> supplier) {
    public T get() {
        return supplier.get();
    }
}
