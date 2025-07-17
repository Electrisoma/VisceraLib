package net.electrisoma.visceralib.api.registration;

import net.electrisoma.visceralib.multiloader.VisceralInitHook;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Central registry manager for deferred registers.
 * Platform-specific code must provide the factory implementation.
 */
public final class VisceralRegistries {
    static {
        VisceralInitHook.bootstrap();
    }

    private static final Map<String, Map<ResourceKey<?>, VisceralDeferredRegister<?>>> REGISTRIES = new HashMap<>();

    @FunctionalInterface
    public interface DeferredRegisterFactory {
        <T> VisceralDeferredRegister<T> create(String modId, ResourceKey<? extends Registry<T>> registryKey);
    }

    private static DeferredRegisterFactory FACTORY;

    /**
     * Set the platform-specific factory. Must be called during platform initialization.
     */
    public static void setFactory(DeferredRegisterFactory factory) {
        if (factory == null)
            throw new IllegalArgumentException("DeferredRegisterFactory cannot be null");
        FACTORY = factory;
    }

    /**
     * Create or get an existing deferred register for the given mod ID and registry key.
     * Thread-safe.
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> VisceralDeferredRegister<T> getOrCreate(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        if (modId == null || modId.isEmpty())
            throw new IllegalArgumentException("modId must not be null or empty");
        if (FACTORY == null)
            throw new IllegalStateException("DeferredRegisterFactory not set! Platform must set the factory before usage.");

        Map<ResourceKey<?>, VisceralDeferredRegister<?>> modRegistries = REGISTRIES.computeIfAbsent(modId, k -> new HashMap<>());
        return (VisceralDeferredRegister<T>) modRegistries.computeIfAbsent(registryKey, key -> FACTORY.create(modId, registryKey));
    }

    public static synchronized Collection<VisceralDeferredRegister<?>> getAllForMod(String modId) {
        Map<ResourceKey<?>, VisceralDeferredRegister<?>> modRegs = REGISTRIES.get(modId);
        if (modRegs == null)
            return Collections.emptyList();
        return Collections.unmodifiableCollection(modRegs.values());
    }
}