package net.electrisoma.visceralib.api.fabric.registration;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class VisceralDeferredRegisterFabric<T> extends VisceralDeferredRegister<T> {
    public VisceralDeferredRegisterFabric(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        super(modId, registryKey);
    }

    @Override
    public <I extends T> VisceralRegistrySupplier<I> register(@NotNull String name, @NotNull Supplier<I> supplier) {
        ResourceLocation id = createId(name);
        Registry<T> registry = getRegistryInstance(registryKey);

        I value = supplier.get();

        Registry<I> castedRegistry = (Registry<I>) registry;
        Registry.register(castedRegistry, id, value);

        ResourceKey<I> key = ResourceKey.create(
                VisceralDeferredRegister.castKey(registryKey),
                VisceraLib.path(modId, name)
        );

        VisceralRegistrySupplier<I> wrapped = new VisceralRegistrySupplier<>(key, () -> value);

        entries.put(name, wrapped);
        return wrapped;
    }

    @Override
    public void registerToEventBus(@Nullable Object modEventBus) {}

    @SuppressWarnings("unchecked")
    private Registry<T> getRegistryInstance(ResourceKey<? extends Registry<T>> key) {
        return (Registry<T>) BuiltInRegistries.REGISTRY.getOptional(key.location())
                .orElseThrow(() -> new IllegalArgumentException("Unsupported or unknown registry key: " + key.location()));
    }

    private ResourceLocation createId(String name) {
        return VisceraLib.path(modId, name);
    }
}