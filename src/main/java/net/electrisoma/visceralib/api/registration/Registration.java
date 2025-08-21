package net.electrisoma.visceralib.api.registration;

import net.electrisoma.visceralib.mixin.accessor.Holder$ReferenceAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public record Registration<R, T extends R>(ResourceLocation id, Registry<R> registry, Supplier<T> value, Holder.Reference<T> holder) {
    @SuppressWarnings("unchecked")
    public void register() {
        T registered = Registry.register(registry, id, value.get());
        ((Holder$ReferenceAccessor<T>) holder).visceral$bindValue(registered);
    }
}