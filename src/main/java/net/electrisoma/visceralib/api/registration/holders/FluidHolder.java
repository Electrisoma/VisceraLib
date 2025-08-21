package net.electrisoma.visceralib.api.registration.holders;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;

public class FluidHolder<T extends Fluid> extends Holder.Reference<T> {
    public FluidHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(Type.STAND_ALONE, owner, key, null);
    }
}
