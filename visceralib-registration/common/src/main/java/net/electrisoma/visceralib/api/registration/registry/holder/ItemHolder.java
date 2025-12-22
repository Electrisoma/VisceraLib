package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ItemHolder<T extends Item> extends BaseHolder<T> {
    public ItemHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(owner, key);
    }
}