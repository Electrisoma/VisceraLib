package net.electrisoma.visceralib.api.item.v1.client;

import net.minecraft.world.item.ItemStack;

public interface BlockResetHook {

	/**
	 * Determines if block breaking progress should be preserved when the held item stack changes.
	 *
	 * @param oldStack the stack previously used to break the block
	 * @param newStack the new stack replacing it
	 * @return true to allow block breaking to continue, false to reset progress
	 */
	default boolean viscera$shouldContinueBreaking(
			ItemStack oldStack,
			ItemStack newStack
	) {
		return ItemStack.isSameItemSameComponents(oldStack, newStack);
	}
}
