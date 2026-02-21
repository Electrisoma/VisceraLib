package net.electrisoma.visceralib.api.item.v1.client.ext;

import net.electrisoma.visceralib.api.item.v1.client.*;

import net.minecraft.world.item.Item;

/**
 * The master extension interface for {@link Item} subclasses, providing access to
 * client-side rendering hooks via interface injection.
 *
 * <p>Note: This interface is automatically implemented on all items via Mixin.
 * It consolidates various rendering-specific hooks such as armor textures,
 * re-equip animations, and block-breaking resets.
 *
 * <p>To utilize these hooks, override the desired methods directly in your
 * {@link Item} class.
 */
public interface VisceralClientItemHooks extends
		HighlightTipHook,
		ArmorTextureHook,
		ReequipAnimationHook,
		BlockResetHook,
		ArmPoseHook
{

	// no-op
}
