package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.DurabilityHook;
import net.electrisoma.visceralib.api.item.v1.EnchantmentHook;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@ModifyReturnValue(method = "modifyDurabilityToRepairFromXp", at = @At("RETURN"))
	private static int viscera$applyCustomMendingRatio(
			int original,
			ServerLevel level,
			ItemStack stack
	) {
		DurabilityHook hook = stack.getItem();
		float ratio = hook.viscera$getMendingEfficiency(stack);
		if (ratio != 1.0f) {
			return Math.round(original * ratio);
		}
		return original;
	}

	@WrapOperation(
			method = "getEnchantmentCost",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getEnchantmentValue()I")
	)
	private static int viscera$wrapEnchantmentCost(
			Item instance,
			Operation<Integer> original,
			@Local(argsOnly = true) ItemStack stack
	) {
		EnchantmentHook hook = stack.getItem();
		return hook.viscera$getEnchantmentValue(stack);
	}

	@WrapOperation(
			method = "selectEnchantment",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getEnchantmentValue()I")
	)
	private static int viscera$wrapSelectEnchantment(
			Item instance,
			Operation<Integer> original,
			@Local(argsOnly = true) ItemStack stack
	) {
		EnchantmentHook hook = stack.getItem();
		return hook.viscera$getEnchantmentValue(stack);
	}
}
