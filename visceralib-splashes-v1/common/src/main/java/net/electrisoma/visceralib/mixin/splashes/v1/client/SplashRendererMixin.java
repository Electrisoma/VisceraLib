package net.electrisoma.visceralib.mixin.splashes.v1.client;

import net.electrisoma.visceralib.api.splashes.v1.client.SplashStyle;
import net.electrisoma.visceralib.api.splashes.v1.client.VisceralSplashStorage;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashRenderer.class)
public class SplashRendererMixin {

	@Inject(
			method = "render",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V")
	)
	private void applyCustomTransform(GuiGraphics graphics, int width, Font font, int color, CallbackInfo ci) {
		SplashStyle style = VisceralSplashStorage.getStyle();
		graphics.pose().scale(style.scale, style.scale, style.scale);
	}

	@ModifyArg(
			method = "render",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"),
			index = 4
	)
	private int changeColor(int originalColor) {
		return VisceralSplashStorage.getStyle().getFinalColor();
	}
}
