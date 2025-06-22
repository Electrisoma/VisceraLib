package net.electrisoma.visceralib.api.registration;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class VisceralDeferredRegisterFabric<T> extends VisceralDeferredRegister<T> {
    private final Map<String, VisceralRegistrySupplier<T>> entries = new HashMap<>();

    public VisceralDeferredRegisterFabric(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        super(modId, registryKey);
    }

    @Override
    public VisceralRegistrySupplier<T> register(@NotNull String name, @NotNull Supplier<T> supplier) {
        ResourceLocation id = createId(name);
        Registry<T> registry = getRegistryInstance(registryKey);

        T value = supplier.get();
        Registry.register(registry, id, value);

        VisceralRegistrySupplier<T> wrapped = new VisceralRegistrySupplier<>(() -> value);
        entries.put(name, wrapped);

        return wrapped;
    }

    @Override
    public void registerToEventBus(@Nullable Object modEventBus) {}

    @SuppressWarnings("unchecked")
    private Registry<T> getRegistryInstance(ResourceKey<? extends Registry<T>> key) {
        if (key == Registries.ITEM) return (Registry<T>) Registries.ITEM;
        if (key == Registries.BLOCK) return (Registry<T>) Registries.BLOCK;

        throw new IllegalArgumentException("Unsupported registry key: " + key.location());
    }

    private ResourceLocation createId(String name) {
        return ResourceLocation.fromNamespaceAndPath(modId, name);
    }
}
