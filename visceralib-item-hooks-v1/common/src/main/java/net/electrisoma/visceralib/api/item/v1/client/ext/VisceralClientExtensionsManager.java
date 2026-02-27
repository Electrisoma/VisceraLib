package net.electrisoma.visceralib.api.item.v1.client.ext;

import net.electrisoma.visceralib.api.item.v1.ext.VisceralItemHooks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.IdentityHashMap;
import java.util.Map;

public final class VisceralClientExtensionsManager {

	private VisceralClientExtensionsManager() {}

	private static final Map<Item, VisceralClientItemHooks> ITEM_EXTENSIONS = new IdentityHashMap<>();
	private static boolean initialized = false;

	public static void init() {
		if (initialized) return;

		BuiltInRegistries.ITEM.forEach(item -> {
			if (item instanceof VisceralItemHooks visceralItem) {
				visceralItem.initializeVisceralClient(ext -> {
					if (ext instanceof VisceralClientItemHooks clientExt)
						ITEM_EXTENSIONS.put(item, clientExt);
				});
			}
		});

		initialized = true;
	}

	public static VisceralClientItemHooks get(Item item) {
		if (!initialized)
			init();
		return ITEM_EXTENSIONS.getOrDefault(item, VisceralClientItemHooks.DEFAULT);
	}
}
