package net.electrisoma.visceralib.impl.dsp.v1.client;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.dsp.v1.DSPPipelineReloadListener;
import net.electrisoma.visceralib.impl.dsp.v1.Constants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class ClientEntrypointFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
		onRegisterReloadListeners();
	}

	private void onRegisterReloadListeners() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {

			private final DSPPipelineReloadListener loader = new DSPPipelineReloadListener();

			@Override
			public ResourceLocation getFabricId() {
				return RLUtils.path(Constants.MOD_ID, DSPPipelineReloadListener.DIR);
			}

			@Override
			public @NotNull CompletableFuture<Void> reload(
					@NotNull PreparationBarrier barrier,
					@NotNull ResourceManager manager,
					@NotNull ProfilerFiller preparationsProfiler,
					@NotNull ProfilerFiller reloadProfiler,
					@NotNull Executor backgroundExecutor,
					@NotNull Executor gameExecutor
			) {
				return loader.reload(
						barrier,
						manager,
						preparationsProfiler,
						reloadProfiler,
						backgroundExecutor,
						gameExecutor
				);
			}
		});
	}
}
