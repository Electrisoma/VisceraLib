package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

public interface PiglinHook {

	/**
	 * Determines if this item can be used to barter with Piglins.
	 * <p>
	 * By default, this checks the {@code #minecraft:piglin_loved} tag.
	 *
	 * @param stack the item stack being bartered.
	 */
	default boolean viscera$isBarterCurrency(ItemStack stack) {
		return stack.is(ItemTags.PIGLIN_LOVED);
	}

	/**
	 * Determines if wearing this item prevents Piglins from becoming aggressive.
	 * <p>
	 * Used to implement "Gold Armor" logic for custom materials or special equipment.
	 *
	 * @param stack  the armor being worn.
	 * @param wearer the entity wearing the armor.
	 */
	default boolean viscera$doesPacifyPiglin(
			ItemStack stack,
			LivingEntity wearer
	) {
		return stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == ArmorMaterials.GOLD;
	}
}
