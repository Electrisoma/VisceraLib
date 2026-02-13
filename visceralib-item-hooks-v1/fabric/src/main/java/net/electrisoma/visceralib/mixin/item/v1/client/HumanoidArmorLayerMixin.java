package net.electrisoma.visceralib.mixin.item.v1.client;

import net.electrisoma.visceralib.api.item.v1.client.ArmorTextureHook;

import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {

	@WrapOperation(
			method = "renderArmorPiece",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ArmorMaterial$Layer;texture(Z)Lnet/minecraft/resources/ResourceLocation;"
			)
	)
	private ResourceLocation viscera$overrideArmorTexture(
			ArmorMaterial.Layer layer,
			boolean inner,
			Operation<ResourceLocation> original,
			@Local(argsOnly = true) LivingEntity entity,
			@Local(argsOnly = true) EquipmentSlot slot,
			@Local(ordinal = 0) ItemStack stack
	) {
		ArmorTextureHook hook = stack.getItem();
		return hook.viscera$getArmorTexture(stack, entity, slot, layer, inner) != null
				? hook.viscera$getArmorTexture(stack, entity, slot, layer, inner)
				: original.call(layer, inner);
	}
}
