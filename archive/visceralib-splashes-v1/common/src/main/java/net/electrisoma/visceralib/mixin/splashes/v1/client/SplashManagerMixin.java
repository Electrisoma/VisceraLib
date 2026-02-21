package net.electrisoma.visceralib.mixin.splashes.v1.client;

import net.electrisoma.visceralib.api.splashes.v1.client.VisceralSplashStorage;

import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {

	@Inject(
			method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;" +
					"Lnet/minecraft/util/profiling/ProfilerFiller;" +
					")Ljava/util/List;",
			at = @At("RETURN"),
			cancellable = true
	)
	private void injectAllSplashes(
			ResourceManager resourceManager,
			ProfilerFiller profiler,
			CallbackInfoReturnable<List<String>> cir
	) {
		List<String> customSplashes = VisceralSplashStorage.loadAll(resourceManager);

		if (customSplashes.isEmpty())
			return;

		List<String> combinedSplashes = new ArrayList<>(cir.getReturnValue());
		combinedSplashes.addAll(customSplashes);

		cir.setReturnValue(combinedSplashes);
	}

	@Inject(method = "getSplash", at = @At("HEAD"), cancellable = true)
	private void injectCustomSeasonalPriority(CallbackInfoReturnable<SplashRenderer> cir) {
		String prioritySplash = VisceralSplashStorage.getPrioritySplash();

		if (prioritySplash != null) {
			cir.setReturnValue(new SplashRenderer(prioritySplash));
		}
	}
}
