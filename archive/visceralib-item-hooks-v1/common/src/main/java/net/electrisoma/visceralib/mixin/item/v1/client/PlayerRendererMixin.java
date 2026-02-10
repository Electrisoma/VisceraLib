package net.electrisoma.visceralib.mixin.item.v1.client;

import net.electrisoma.visceralib.api.item.v1.client.ArmPoseHook;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
    private static void viscera$injectCustomArmPose(
            AbstractClientPlayer player,
            InteractionHand hand,
            CallbackInfoReturnable<HumanoidModel.ArmPose> cir
    ) {
        ItemStack stack = player.getItemInHand(hand);
        ArmPoseHook hook = stack.getItem();
        HumanoidModel.ArmPose pose = hook.viscera$getArmPose(stack, player, hand);
        if (pose != null) {
            cir.setReturnValue(pose);
        }
    }
}