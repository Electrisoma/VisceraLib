package net.electrisoma.visceralib.api.item.v1.mixin;

import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class IClientItemExtensionsInternal {

	public static final Map<Object, Item> EXTENSION_CACHE = Collections.synchronizedMap(new WeakHashMap<>());
}
