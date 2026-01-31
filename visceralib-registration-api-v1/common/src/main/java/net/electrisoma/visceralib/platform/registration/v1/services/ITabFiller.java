package net.electrisoma.visceralib.platform.registration.v1.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface ITabFiller {

    ITabFiller INSTANCE = ServiceHelper.load(ITabFiller.class);

    void registerListeners();

    void addBinding(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> item);
}
