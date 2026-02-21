package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public interface StackHook {

	/**
	 * Defines the maximum number of items that can fit into a stack.
	 * <p>
	 * Override this to implement dynamic stack sizes
	 * without permanently modifying the base Item's components.
	 *
	 * @param stack the specific instance of the item being checked.
	 * @return the maximum allowable size for this stack, typically between 1 and 99.
	 */
	default int viscera$getStackLimit(ItemStack stack) {
		return stack.getOrDefault(DataComponents.MAX_STACK_SIZE, 64);
	}
}
