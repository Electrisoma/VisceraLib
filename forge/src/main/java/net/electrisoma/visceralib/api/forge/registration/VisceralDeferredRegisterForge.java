package net.electrisoma.visceralib.api.forge.registration;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class VisceralDeferredRegisterForge<T> extends VisceralDeferredRegister<T> {
    private final DeferredRegister<T> deferred;
    private static IEventBus EVENT_BUS;

    public VisceralDeferredRegisterForge(String modId, ResourceKey<? extends Registry<T>> registryKey) {
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
        RegistryObject<T> registryObject = deferred.register(name, supplier);
        ResourceKey<T> key = ResourceKey.create(this.registryKey, VisceraLib.path(modId, name));
        VisceralRegistrySupplier<T> wrapped = new VisceralRegistrySupplier<>(key, registryObject::get);
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
            throw new IllegalArgumentException("Forge event bus is required to register.");
        }
    }
}
