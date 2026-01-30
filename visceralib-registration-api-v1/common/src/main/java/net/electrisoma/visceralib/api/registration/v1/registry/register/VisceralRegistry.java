package net.electrisoma.visceralib.api.registration.v1.registry.register;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.mixin.registration.v1.accessor.Holder$ReferenceAccessor;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public record VisceralRegistry(String modId) {

    private static final Logger LOG = LoggerFactory.getLogger("VisceralRegistry");

    private static final Multimap<ResourceKey<?>, Registration<?, ?>> ENTRIES =
            Multimaps.newMultimap(new ConcurrentHashMap<>(), ConcurrentLinkedQueue::new);
    public static final Multimap<ResourceKey<?>, Registration<?, ?>> ENTRIES_VIEW =
            Multimaps.unmodifiableMultimap(ENTRIES);

    @SuppressWarnings("unchecked")
    public <R, T extends R> RegistryObject<T> register(
            Registry<R> registry,
            String name,
            Supplier<T> supplier
    ) {
        ResourceLocation id = RLUtils.path(modId, name);
        ResourceKey<T> key = ResourceKey.create((ResourceKey<? extends Registry<T>>) registry.key(), id);
        HolderOwner<T> owner = (HolderOwner<T>) registry.holderOwner();

        RegistryObject<T> holder = new RegistryObject<>(owner, key);
        Registration<R, T> registration = new Registration<>(id, registry, supplier, holder);

        var existing = ENTRIES.get(registry.key()).stream()
                .filter(r -> r.id().equals(id))
                .findFirst();

        if (existing.isPresent()) {
            LOG.error("Duplicate registration for ID '{}'. Returning original holder.", id);
            return (RegistryObject<T>) existing.get().holder();
        }

        ENTRIES.put(registry.key(), registration);

        if (IPlatformHelper.INSTANCE.isCurrent(IPlatformHelper.PlatformEnum.FABRIC))
            registration.register();

        return holder;
    }

    public record Registration<R, T extends R>(
            ResourceLocation id,
            Registry<R> registry,
            Supplier<T> value,
            RegistryObject<T> holder,
            AtomicBoolean registered
    ) {
        public Registration(
                ResourceLocation id,
                Registry<R> registry,
                Supplier<T> value,
                RegistryObject<T> holder
        ) {
            this(id, registry, value, holder, new AtomicBoolean(false));
        }

        public void register() {
            if (registered.compareAndSet(false, true)) {
                T registeredValue = Registry.register(registry, id, value.get());
                //noinspection unchecked
                ((Holder$ReferenceAccessor<T>) holder).viscera$bindValue(registeredValue);
            }
        }
    }
}