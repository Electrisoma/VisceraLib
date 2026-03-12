package net.electrisoma.visceralib.impl.model.loading.v1.client;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.model.loading.v1.client.obj.VisceralObjLoader;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import org.jetbrains.annotations.NotNull;

public final class ClientEntrypointFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
		initializeResourceReloadListeners();
	}

	private void initializeResourceReloadListeners() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
				new SimpleSynchronousResourceReloadListener() {

					@Override
					public ResourceLocation getFabricId() {
						return RLUtils.path("visceralib", "obj_reload");
					}

					@Override
					public void onResourceManagerReload(@NotNull ResourceManager manager) {
						VisceralObjLoader.clearCache();
					}
				}
		);
	}
}
