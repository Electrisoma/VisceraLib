package net.electrisoma.visceralib.registry.holder;

import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemHolder<T extends Item> extends BaseHolder<T> {

    public ItemHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(owner, key);
    }
}