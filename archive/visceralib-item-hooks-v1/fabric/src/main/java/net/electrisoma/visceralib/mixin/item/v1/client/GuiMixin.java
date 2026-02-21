package net.electrisoma.visceralib.mixin.item.v1.client;

import net.electrisoma.visceralib.api.item.v1.client.HighlightTipHook;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Shadow private ItemStack lastToolHighlight;

	@ModifyVariable(
			method = "renderSelectedItemName",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"),
			ordinal = 0
	)
	private MutableComponent viscera$applyHighlightTip(MutableComponent original) {
		HighlightTipHook hook = this.lastToolHighlight.getItem();
		Component result = hook.viscera$getHighlightedName(this.lastToolHighlight, original);
		return result instanceof MutableComponent mc ? mc : result.copy();
	}
}
