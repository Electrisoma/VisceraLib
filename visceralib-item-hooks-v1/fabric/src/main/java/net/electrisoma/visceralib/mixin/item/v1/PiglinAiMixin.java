package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.PiglinHook;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

	@Inject(method = "isWearingGold", at = @At("RETURN"), cancellable = true)
	private static void viscera$bridgePiglinNeutrality(
			LivingEntity wearer,
			CallbackInfoReturnable<Boolean> cir
	) {
		if (cir.getReturnValue())
			return;
		for (ItemStack stack : wearer.getArmorSlots()) {
			PiglinHook hook = stack.getItem();
			if (hook.viscera$doesPacifyPiglin(stack, wearer)) {
				cir.setReturnValue(true);
				return;
			}
		}
	}

	@Inject(method = "isBarterCurrency", at = @At("RETURN"), cancellable = true)
	private static void viscera$bridgePiglinCurrency(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue())
			return;
		PiglinHook hook = stack.getItem();
		if (hook.viscera$isBarterCurrency(stack)) {
			cir.setReturnValue(true);
		}
	}
}
