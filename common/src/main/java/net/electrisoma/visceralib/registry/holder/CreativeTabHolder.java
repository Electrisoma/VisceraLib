package net.electrisoma.visceralib.registry.holder;

import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class CreativeTabHolder extends BaseHolder<CreativeModeTab> {

    public CreativeTabHolder(HolderOwner<CreativeModeTab> owner, ResourceKey<CreativeModeTab> key) {
        super(owner, key);
    }
}