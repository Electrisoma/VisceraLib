package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.world.level.material.Fluid;

public record FluidEntry<T extends Fluid>(VisceralRegistrySupplier<T> supplier) {
    public T get() {
        return supplier.get();
    }
}
