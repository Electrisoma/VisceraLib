package net.electrisoma.resotech.api.registration.builders;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType.EntityFactory;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class EntityBuilder<T extends net.minecraft.world.entity.Entity> {
    private final DeferredRegister<EntityType<?>> deferredRegister;
    private final String name;
    private EntityFactory<T> factory;
    private MobCategory category;
    private int width = 0;
    private int height = 0;
    private int clientTrackingRange = 8;
    private boolean updateIntervalSet = false;
    private int updateInterval = 3;
    private boolean receivesVelocityUpdates = true;

    public EntityBuilder(DeferredRegister<EntityType<?>> deferredRegister, String name) {
        this.deferredRegister = deferredRegister;
        this.name = name;
    }

    public EntityBuilder<T> factory(EntityFactory<T> factory) {
        this.factory = factory;
        return this;
    }

    public EntityBuilder<T> category(MobCategory category) {
        this.category = category;
        return this;
    }

    public EntityBuilder<T> size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public EntityBuilder<T> trackingRange(int range) {
        this.clientTrackingRange = range;
        return this;
    }

    public EntityBuilder<T> updateInterval(int interval) {
        this.updateIntervalSet = true;
        this.updateInterval = interval;
        return this;
    }

    public EntityBuilder<T> receivesVelocityUpdates(boolean flag) {
        this.receivesVelocityUpdates = flag;
        return this;
    }

    public RegistrySupplier<EntityType<T>> register() {
        if (factory == null || category == null || width == 0 || height == 0) {
            throw new IllegalStateException("Must set factory, category, and size before registering entity");
        }

        Supplier<EntityType<T>> supplier = () -> EntityType.Builder.of(factory, category)
                .sized(width, height)
                .clientTrackingRange(clientTrackingRange)
                .updateInterval(updateInterval)
                .build(name);

        return deferredRegister.register(name, supplier);
    }
}
