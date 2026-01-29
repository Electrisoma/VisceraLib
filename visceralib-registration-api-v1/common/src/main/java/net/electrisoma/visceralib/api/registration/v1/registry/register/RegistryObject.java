package net.electrisoma.visceralib.api.registration.v1.registry.register;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;

public class RegistryObject<T> extends Holder.Reference<T> {

    public RegistryObject(HolderOwner<T> owner, ResourceKey<T> key) {
        super(Type.STAND_ALONE, owner, key, null);
    }

    public T get() {
        return value();
    }
}