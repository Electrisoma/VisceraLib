package net.electrisoma.visceralib.platform.model.loading.v1.event.client;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.VisceralUnbakedGeometry;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.IVisceralGeometryContext;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.VisceralGeometryContext;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.VisceralGeometryLoader;
import net.electrisoma.visceralib.event.model.loading.v1.client.VisceralModelEvent;
import net.electrisoma.visceralib.event.model.loading.v1.client.VisceralModelEventHandlers;
import net.electrisoma.visceralib.platform.model.loading.v1.service.event.client.VisceraLibModelEvents;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import com.google.auto.service.AutoService;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@AutoService(VisceraLibModelEvents.class)
public final class VisceraLibModelClientEventsImpl implements VisceraLibModelEvents {

	private static boolean initialized = false;

	private static void ensureInitialized() {
		if (initialized) return;
		initialized = true;

		ModelLoadingPlugin.register(context -> {
			VisceralModelEventHandlers.fireRegisterAdditional(
					new VisceralModelEvent.RegisterAdditional(obj -> {
						if (obj instanceof ModelResourceLocation mrl) {
							context.addModels(mrl.id());
						} else if (obj instanceof ResourceLocation rl) {
							context.addModels(rl);
						}
					})
			);

			context.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, modContext) -> {
				ModelResourceLocation topId = modContext.topLevelId();
				if (topId == null) return model;

				Map<ModelResourceLocation, BakedModel> tempMap = new HashMap<>();
				tempMap.put(topId, model);

				VisceralModelEventHandlers.fireModifyBakingResult(new VisceralModelEvent.ModifyBakingResult(tempMap));

				return tempMap.get(topId);
			});

			context.resolveModel().register(resolveCtx -> {
				JsonObject json = loadModelJson(resolveCtx.id());
				if (json != null && json.has("loader")) {
					ResourceLocation loaderId = RLUtils.parse(json.get("loader").getAsString());

					Map<ResourceLocation, VisceralGeometryLoader> loaders = new HashMap<>();
					VisceralModelEventHandlers.fireRegisterGeometryLoaders(
							new VisceralModelEvent.RegisterGeometryLoaders(loaders::put)
					);

					VisceralGeometryLoader loader = loaders.get(loaderId);
					if (loader != null) {
						VisceralUnbakedGeometry geom = loader.read(json);
						return new UnbakedModel() {

							@Override
							public @NotNull Collection<ResourceLocation> getDependencies() {
								if (json.has("parent"))
									return List.of(RLUtils.parse(json.get("parent").getAsString()));
								return Collections.emptyList();
							}

							@Override
							public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter) {
								IVisceralGeometryContext discoveryContext = new VisceralGeometryContext(json, modelGetter);
								for (Material mat : geom.getMaterials(discoveryContext))
									modelGetter.apply(mat.texture());
							}

							@Override
							public BakedModel bake(
									@NotNull ModelBaker baker,
									@NotNull Function<Material, TextureAtlasSprite> getter,
									@NotNull ModelState state
							) {
								IVisceralGeometryContext visceralContext = new VisceralGeometryContext(json, baker);
								return geom.bake(visceralContext, baker, getter, state);}
						};
					}
				}

				return null;
			});
		});
	}

	@Override
	public void onModifyBakingResult(Consumer<VisceralModelEvent.ModifyBakingResult> handler) {
		VisceralModelEventHandlers.addBakingHandler(handler);
		ensureInitialized();
	}

	@Override
	public void onRegisterAdditional(Consumer<VisceralModelEvent.RegisterAdditional> handler) {
		VisceralModelEventHandlers.addAdditionalHandler(handler);
		ensureInitialized();
	}

	@Override
	public void onRegisterGeometryLoaders(Consumer<VisceralModelEvent.RegisterGeometryLoaders> handler) {
		VisceralModelEventHandlers.addGeometryHandler(handler);
		ensureInitialized();
	}

	private static JsonObject loadModelJson(ResourceLocation id) {
		ResourceLocation fileLocation = RLUtils.path(
				id.getNamespace(),
				"models/" + id.getPath() + ".json"
		);

		return Minecraft.getInstance().getResourceManager().getResource(fileLocation)
				.map(resource -> {
					try (BufferedReader reader = resource.openAsReader()) {
						return net.minecraft.util.GsonHelper.parse(reader);
					} catch (Exception e) {
						return null;
					}
				}).orElse(null);
	}
}
