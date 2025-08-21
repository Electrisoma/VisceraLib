package net.electrisoma.visceralib.api.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Abstract base class for VisceraLib's cross-platform deferred registration.
 * Platform-specific implementations must extend this.
 */
public abstract class VisceralDeferredRegister<T> {
    protected final String modId;
    protected final Map<String, VisceralRegistrySupplier<? extends T>> entries = new HashMap<>();
    protected final ResourceKey<? extends Registry<T>> registryKey;

    protected VisceralDeferredRegister(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        this.modId = modId;
        this.registryKey = registryKey;
    }

    /**
     * Factory method to create a deferred register. Mod ID must be provided explicitly.
     */
    public static <T> VisceralDeferredRegister<T> create(ResourceKey<Registry<T>> registryKey, String modId) {
        return VisceralRegistries.getOrCreate(registryKey, modId);
    }

    /**
     * Register a new object under the given name.
     *
     * @param name     registry name
     * @param supplier supplier of the registered object
     * @return a supplier wrapper around the registered object
     */
    public abstract <I extends T> VisceralRegistrySupplier<I> register(String name, Supplier<I> supplier);

    /**
     * Register this deferred register to the mod event bus (Forge-specific).
     *
     * @param modEventBus the mod event bus instance or null if not applicable
     */
    public abstract void registerToEventBus(@Nullable Object modEventBus);

    /**
     * Get an unmodifiable view of all registered entries.
     */
    public Collection<VisceralRegistrySupplier<? extends T>> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public static <R> ResourceKey<R> castKey(ResourceKey<?> key) {
        //noinspection unchecked
        return (ResourceKey<R>) key;
    }

    public String getModId() {
        return modId;
    }
}