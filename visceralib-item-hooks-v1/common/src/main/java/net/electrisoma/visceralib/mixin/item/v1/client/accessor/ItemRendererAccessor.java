package net.electrisoma.visceralib.mixin.item.v1.client.accessor;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {

	@Accessor("blockEntityRenderer")
	BlockEntityWithoutLevelRenderer visceralib$getBlockEntityRenderer();
}
