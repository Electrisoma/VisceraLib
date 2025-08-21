package net.electrisoma.visceralib.api.registration.holders;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class BlockHolder<T extends Block> extends Holder.Reference<T> {
    public BlockHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(Type.STAND_ALONE, owner, key, null);
    }
}