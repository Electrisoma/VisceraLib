package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.EnchantmentHook;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

	@Inject(
			method = "createResult",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;set(Lnet/minecraft/core/Holder;I)V"),
			cancellable = true
	)
	private void viscera$bridgeIsBookEnchantable(CallbackInfo ci) {
		AnvilMenu menu = (AnvilMenu)(Object)this;
		ItemStack leftStack = menu.getSlot(0).getItem();
		ItemStack rightStack = menu.getSlot(1).getItem();

		if (leftStack.getItem() instanceof EnchantmentHook hook) {
			if (!hook.viscera$isEnchantableWithBook(leftStack, rightStack)) {
				menu.getSlot(2).set(ItemStack.EMPTY);
				ci.cancel();
			}
		}
	}
}
