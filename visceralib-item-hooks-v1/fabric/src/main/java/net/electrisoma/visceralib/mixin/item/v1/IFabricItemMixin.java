package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.CraftingRemainderHook;
import net.electrisoma.visceralib.api.item.v1.EnchantmentHook;
import net.electrisoma.visceralib.api.item.v1.client.BlockResetHook;
import net.electrisoma.visceralib.api.item.v1.client.ReequipAnimationHook;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItem;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FabricItem.class)
public interface IFabricItemMixin {

	@ModifyReturnValue(method = "allowComponentsUpdateAnimation", at = @At("RETURN"))
	private boolean viscera$bridgeReequipAnimationHook(
			boolean original,
			Player player,
			InteractionHand hand,
			ItemStack oldStack,
			ItemStack newStack
	) {
		if ((Object) this instanceof ReequipAnimationHook hooks) {
			return hooks.viscera$shouldAllowReequipAnimation(oldStack, newStack, false);
		}
		return original;
	}

	@ModifyReturnValue(method = "allowContinuingBlockBreaking", at = @At("RETURN"))
	private boolean viscera$bridgeBlockResetHook(
			boolean original,
			Player player,
			ItemStack oldStack,
			ItemStack newStack
	) {
		if ((Object) this instanceof BlockResetHook hooks) {
			if (oldStack.getItem() == newStack.getItem()) {
				return hooks.viscera$shouldContinueBreaking(oldStack, newStack);
			}
		}
		return original;
	}

	@ModifyReturnValue(method = "getRecipeRemainder", at = @At("RETURN"))
	default ItemStack viscera$bridgeCraftingRemainingHook(
			ItemStack original,
			ItemStack stack
	) {
		if ((Object) this instanceof CraftingRemainderHook hook) {
			return hook.viscera$getCraftingRemaining(stack);
		}
		return original;
	}

	@ModifyReturnValue(method = "canBeEnchantedWith", at = @At("RETURN"))
	default boolean viscera$bridgeEnchantmentHook(
			boolean original,
			ItemStack stack,
			Holder<Enchantment> enchantment,
			EnchantingContext context
	) {
		if ((Object) this instanceof EnchantmentHook hook) {
			boolean isPrimary = (context == EnchantingContext.PRIMARY);
			return hook.viscera$isCompatibleWithEnchantment(stack, enchantment, isPrimary);
		}
		return original;
	}
}
