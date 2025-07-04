package net.electrisoma.visceralib.api.neoforge.registration;

import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class VisceralDeferredRegisterNeoForge<T> extends VisceralDeferredRegister<T> {
    private final DeferredRegister<T> deferred;
    private static IEventBus EVENT_BUS;

    public VisceralDeferredRegisterNeoForge(String modId, ResourceKey<? extends Registry<T>> registryKey) {
        super(modId, registryKey);
        this.deferred = DeferredRegister.create(registryKey, modId);

        if (EVENT_BUS != null) {
            this.deferred.register(EVENT_BUS);
        } else {
            throw new IllegalStateException("EVENT_BUS not initialized before registry creation for " + modId);
        }
    }

    @Override
    public VisceralRegistrySupplier<T> register(@NotNull String name, @NotNull Supplier<T> supplier) {
        DeferredHolder<T, T> holder = deferred.register(name, supplier);
        ResourceKey<T> key = ResourceKey.create(this.registryKey, ResourceLocation.fromNamespaceAndPath(modId, name));
        VisceralRegistrySupplier<T> wrapped = new VisceralRegistrySupplier<>(key, holder);
        entries.put(name, wrapped);
        return wrapped;
    }

    public static void setEventBus(IEventBus bus) {
        EVENT_BUS = bus;
    }

    public void registerToEventBus(@Nullable Object modEventBus) {
        if (modEventBus instanceof IEventBus bus) {
            deferred.register(bus);
        } else {
            throw new IllegalArgumentException("NeoForge event bus is required to register.");
        }
    }
}