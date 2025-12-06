package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;

public class BaseHolder<T> extends Holder.Reference<T> {

    protected BaseHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(Type.STAND_ALONE, owner, key, null);
    }

    public <V> boolean is(V value) {
        return value() == value;
    }

    public T get() {
        return value();
    }
}