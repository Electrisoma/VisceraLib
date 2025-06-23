package net.electrisoma.visceralib.fabric;

import net.electrisoma.visceralib.api.registration.CreativeTabBuilderRegistry;
import net.electrisoma.visceralib.api.registration.helpers.ICreativeTabOutputs;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabFillerFabric {
    public static void register() {
        for (ICreativeTabOutputs builder : CreativeTabBuilderRegistry.getAllBuilders()) {
            for (ResourceKey<CreativeModeTab> tabKey : builder.getTabs())
                ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries -> {
                    for (ItemStack stack : builder.getTabContents())
                        entries.accept(stack);
                });
        }
    }
}
