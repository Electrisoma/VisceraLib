package net.electrisoma.visceralib.api.registration.helpers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public interface ICreativeTabOutputs {
    static Collection<? extends ICreativeTabOutputs> getAllBuilders() {
        throw new UnsupportedOperationException("Must be overridden");
    }

    Set<ResourceKey<CreativeModeTab>> getTabs();
    Collection<ItemStack> getTabContents();
}
