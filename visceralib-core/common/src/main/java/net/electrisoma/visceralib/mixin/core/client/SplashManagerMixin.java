package net.electrisoma.visceralib.mixin.core.client;

import net.electrisoma.visceralib.api.core.client.splashes.SplashStorage;
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
            method = "prepare(" +
                    "Lnet/minecraft/server/packs/resources/ResourceManager;" +
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
        List<String> vanilla = cir.getReturnValue();
        List<String> vanillaSplashes = (vanilla != null)
                ? new ArrayList<>(vanilla) : new ArrayList<>();

        List<String> customSplashes = SplashStorage.fetchAllSplashes(resourceManager);

        if (customSplashes.isEmpty())
            return;

        vanillaSplashes.addAll(customSplashes);
        cir.setReturnValue(vanillaSplashes);
    }
}