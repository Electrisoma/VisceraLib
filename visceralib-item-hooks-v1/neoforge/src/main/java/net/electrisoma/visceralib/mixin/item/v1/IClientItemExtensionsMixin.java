package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.client.ext.VisceralClientExtensionsManager;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IClientItemExtensions.class)
public interface IClientItemExtensionsMixin {

	@ModifyReturnValue(method = "getArmPose", at = @At("RETURN"))
	default HumanoidModel.@Nullable ArmPose viscera$bridgeArmPoseHook(
			HumanoidModel.@Nullable ArmPose original,
			LivingEntity entity,
			InteractionHand hand,
			ItemStack stack
	) {
		HumanoidModel.ArmPose custom = VisceralClientExtensionsManager.get(stack.getItem())
				.viscera$getArmPose(stack, (AbstractClientPlayer) entity, hand);

		return custom != null ? custom : original;
	}
}
