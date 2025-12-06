package net.electrisoma.visceralib.api.registration.registry;

import net.electrisoma.visceralib.mixin.registration.accessor.Holder$ReferenceAccessor;
import net.electrisoma.visceralib.api.registration.registry.holder.BaseHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record Registration<R, T extends R, H extends BaseHolder<T>>(
        ResourceLocation id, Registry<R> registry,
        Supplier<T> value, H holder,
        Consumer<H> afterRegisterCallback
) {

    @SuppressWarnings("unchecked")
    public void register() {
        T registered = Registry.register(registry, id, value.get());
        ((Holder$ReferenceAccessor<T>) holder).visceral$bindValue(registered);
        afterRegisterCallback.accept(holder);
    }
}