package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface EquipmentHook {

	/**
	 * Determines if the item is permitted to occupy a specific equipment slot.
	 * <p>
	 * Override this to allow items to be placed in non-standard slots
	 * or to restrict specific entities from equipping certain items.
	 *
	 * @param stack  the item stack being checked.
	 * @param slot   the slot the item is attempting to enter.
	 * @param entity the entity attempting to equip the item.
	 * @return true if the item is compatible with the slot.
	 */
	default boolean viscera$canFitSlot(
			ItemStack stack,
			EquipmentSlot slot,
			LivingEntity entity
	) {
		return entity.getEquipmentSlotForItem(stack) == slot;
	}

	/**
	 * Defines the natural or preferred equipment slot for this item.
	 * <p>
	 * This is used by the game to determine which slot to fill when a player
	 * right-clicks the item from their hotbar or when an entity is spawned with gear.
	 *
	 * @param stack the item stack being checked.
	 * @return the target EquipmentSlot, or null if it has no default slot.
	 */
	@Nullable
	default EquipmentSlot viscera$getDesiredSlot(ItemStack stack) {
		return null;
	}
}
