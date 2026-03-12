package net.electrisoma.visceralib.api.item.v1.client;

import net.electrisoma.visceralib.mixin.item.v1.client.accessor.ItemRendererAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public interface CustomRendererHook {

	default BlockEntityWithoutLevelRenderer viscera$getCustomRenderer() {
		return ((ItemRendererAccessor) Minecraft.getInstance().getItemRenderer())
				.visceralib$getBlockEntityRenderer();
	}
}
