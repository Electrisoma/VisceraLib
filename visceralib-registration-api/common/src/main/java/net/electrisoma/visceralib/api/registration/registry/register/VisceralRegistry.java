package net.electrisoma.visceralib.api.registration.registry.register;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.mixin.registration.accessor.Holder$ReferenceAccessor;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record VisceralRegistry(String modId) {

    private static final Multimap<ResourceKey<?>, Registration<?, ?>> REGISTRATIONS = HashMultimap.create();
    public static final Multimap<ResourceKey<?>, Registration<?, ?>> REGISTRATIONS_VIEW = Multimaps.unmodifiableMultimap(REGISTRATIONS);

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

        if (IPlatformHelper.INSTANCE.isCurrent(IPlatformHelper.PlatformEnum.FABRIC)) {
            registration.register();
        } else {
            REGISTRATIONS.put(registry.key(), registration);
        }

        return holder;
    }

    public record Registration<R, T extends R>(
            ResourceLocation id,
            Registry<R> registry,
            Supplier<T> value,
            RegistryObject<T> holder
    ) {
        @SuppressWarnings("unchecked")
        public void register() {
            T registered = Registry.register(registry, id, value.get());
            ((Holder$ReferenceAccessor<T>) holder).viscera$bindValue(registered);
        }
    }

    public <T extends Item> RegistryObject<T> item(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.ITEM, name, supplier);
    }

    public <T extends Block> RegistryObject<T> block(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.BLOCK, name, supplier);
    }
}