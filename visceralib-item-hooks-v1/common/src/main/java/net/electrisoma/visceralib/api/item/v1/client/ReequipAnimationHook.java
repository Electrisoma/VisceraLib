package net.electrisoma.visceralib.api.item.v1.client;

import net.minecraft.world.item.ItemStack;

public interface ReequipAnimationHook {

	/**
	 * Determines if the re-equip animation should play when the item stack in a hand is updated.
	 *
	 * <p>By default, vanilla triggers this animation whenever the stacks are not considered
	 * identical.
	 * Implementing this and returning {@code false} allows for seamless
	 * component updates without visual disruption.
	 *
	 * @param oldStack    the stack previously held in the hand
	 * @param newStack    the new stack replacing it
	 * @param slotChanged true if the item was moved to a different slot, false if the stack
	 * was updated within the same slot
	 * @return true to play the re-equip animation, false to suppress it
	 */
	default boolean viscera$shouldAllowReequipAnimation(
			ItemStack oldStack,
			ItemStack newStack,
			boolean slotChanged
	) {
		return !oldStack.equals(newStack);
	}
}
