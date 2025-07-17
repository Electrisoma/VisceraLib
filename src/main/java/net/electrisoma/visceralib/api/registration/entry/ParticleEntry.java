package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.particles.ParticleType;

public record ParticleEntry<T extends ParticleType<?>>(VisceralRegistrySupplier<T> supplier) {
    public T get() {
        return supplier.get();
    }
}
