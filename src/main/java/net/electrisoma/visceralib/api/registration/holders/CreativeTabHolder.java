package net.electrisoma.visceralib.api.registration.holders;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class CreativeTabHolder extends Holder.Reference<CreativeModeTab> {
    public CreativeTabHolder(HolderOwner<CreativeModeTab> owner, ResourceKey<CreativeModeTab> key) {
        super(Holder.Reference.Type.STAND_ALONE, owner, key, null);
    }
}
