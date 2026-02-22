package net.electrisoma.visceralib.api.item.v1.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface ArmorTextureHook {

	/**
	 * Supplies the texture location for the armor piece being rendered.
	 * Returning {@code null} will result in the default vanilla texture resolution logic.
	 *
	 * <p>This allows for assigning armor textures to items without requiring a unique
	 * {@link ArmorMaterial} for every aesthetic variation.
	 *
	 * @param stack      the armor item stack
	 * @param entity     the entity wearing the armor
	 * @param slot       the slot the armor is equipped in
	 * @param layer      the current armor material layer being rendered
	 * @param innerModel true if the inner model (leggings) is being rendered, false otherwise
	 * @return the custom texture location, or null to use the default
	 */
	@Nullable
	default ResourceLocation viscera$getArmorTexture(
			ItemStack stack,
			Entity entity,
			EquipmentSlot slot,
			ArmorMaterial.Layer layer,
			boolean innerModel
	) {
		return null;
	}
}
