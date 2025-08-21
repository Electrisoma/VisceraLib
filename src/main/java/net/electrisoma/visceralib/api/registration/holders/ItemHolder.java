package net.electrisoma.visceralib.api.registration.holders;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ItemHolder<T extends Item> extends Holder.Reference<T> {
    public ItemHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(Type.STAND_ALONE, owner, key, null);
    }
}
