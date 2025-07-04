package net.electrisoma.visceralib.api.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected final ResourceKey<? extends Registry<T>> registryKey;
    protected final Map<String, VisceralRegistrySupplier<T>> entries = new HashMap<>();

    protected VisceralDeferredRegister(@NotNull String modId,
                                       @NotNull ResourceKey<? extends Registry<T>> registryKey) {
        this.modId = modId;
        this.registryKey = registryKey;
    }

    /**
     * Factory method to create a deferred register. Mod ID must be provided explicitly.
     */
    public static <T> VisceralDeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return VisceralRegistries.getOrCreate(registryKey, modId);
    }

    /**
     * Register a new object under the given name.
     *
     * @param name     registry name
     * @param supplier supplier of the registered object
     * @return a supplier wrapper around the registered object
     */
    public abstract VisceralRegistrySupplier<T> register(@NotNull String name, @NotNull Supplier<T> supplier);

    /**
     * Register this deferred register to the mod event bus (Forge-specific).
     *
     * @param modEventBus the mod event bus instance or null if not applicable
     */
    public abstract void registerToEventBus(@Nullable Object modEventBus);

    /**
     * Get an unmodifiable view of all registered entries.
     */
    public Map<String, VisceralRegistrySupplier<T>> getEntries() {
        return Collections.unmodifiableMap(entries);
    }
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public String getModId() {
        return modId;
    }
}