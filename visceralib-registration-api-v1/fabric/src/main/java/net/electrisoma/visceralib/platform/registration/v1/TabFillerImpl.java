package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.platform.registration.v1.services.ITabFiller;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import com.google.auto.service.AutoService;

import java.util.function.Supplier;

@AutoService(ITabFiller.class)
public class TabFillerImpl implements ITabFiller {

	@Override
	public void addBinding(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> item) {
		ItemGroupEvents.modifyEntriesEvent(tab).register(entries ->
			entries.accept(item.get())
		);
	}

	@Override
	public void registerListeners() {}
}
