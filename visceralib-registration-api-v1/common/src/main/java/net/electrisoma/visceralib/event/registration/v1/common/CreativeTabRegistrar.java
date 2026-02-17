package net.electrisoma.visceralib.event.registration.v1.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface CreativeTabRegistrar {

	void add(ResourceKey<CreativeModeTab> tab, ItemStack stack, CreativeModeTab.TabVisibility visibility);

	default void add(ResourceKey<CreativeModeTab> tab, ItemStack stack) {
		add(tab, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	default void add(ResourceLocation tabId, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		add(ResourceKey.create(Registries.CREATIVE_MODE_TAB, tabId), stack, visibility);
	}

	default void add(ResourceLocation tabId, ItemStack stack) {
		add(tabId, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}
}
