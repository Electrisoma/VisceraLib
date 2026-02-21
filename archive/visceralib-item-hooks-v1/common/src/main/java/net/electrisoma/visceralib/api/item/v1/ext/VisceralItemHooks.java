package net.electrisoma.visceralib.api.item.v1.ext;

import net.electrisoma.visceralib.api.item.v1.*;

import net.minecraft.world.item.Item;

/**
 * The master extension interface for {@link Item} subclasses, providing access to
 * common (server and client) logic hooks via interface injection.
 *
 * <p>Note: This interface is automatically implemented on all items via Mixin.
 * It serves as a base for cross-platform utility methods that do not rely
 * on client-only classes like {@code Renderer} or {@code Model}.
 *
 * <p>Functions should only be added to this interface if they are general-purpose
 * and applicable to both logical sides.
 */
public interface VisceralItemHooks extends
		CombatHook,
		CraftingRemainderHook,
		DropHook,
		DurabilityHook,
		EnchantmentHook,
		EntityHook,
		EquipmentHook,
		PiglinHook,
		StackHook,
		UseHook
{

	// no-op
}
