package net.electrisoma.visceralib.api.registration.entry;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record BlockEntityEntry<T extends BlockEntity>(VisceralRegistrySupplier<BlockEntityType<T>> supplier) {
    public BlockEntityType<T> get() {
        return supplier.get();
    }
}
