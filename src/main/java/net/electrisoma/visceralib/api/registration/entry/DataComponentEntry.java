package net.electrisoma.visceralib.api.registration.entry;

/*? >=1.21.1 {*/
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.component.DataComponentType;

public record DataComponentEntry<T>(VisceralRegistrySupplier<DataComponentType<T>> supplier) {
    public DataComponentType<T> get() {
        return supplier.get();
    }
}
/*?}*/