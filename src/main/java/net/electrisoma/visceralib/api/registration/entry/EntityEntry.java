package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public record EntityEntry<T extends Entity>(VisceralRegistrySupplier<EntityType<T>> supplier) {
    public EntityType<T> get() {
        return supplier.get();
    }
}
