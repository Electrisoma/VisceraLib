package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public interface CombatHook {

	/**
	 * Determines if an attack with this item should trigger the
	 * shield disable cooldown on the target.
	 *
	 * @param stack    the item being used to attack.
	 * @param shield   the shield stack being blocked with.
	 * @param entity   the defender holding the shield.
	 * @param attacker the entity performing the attack.
	 * @return true if the shield should be disabled.
	 */
	default boolean viscera$canDisableShield(
			ItemStack stack,
			ItemStack shield,
			LivingEntity entity,
			LivingEntity attacker
	) {
		return stack.getItem() instanceof AxeItem;
	}
}
