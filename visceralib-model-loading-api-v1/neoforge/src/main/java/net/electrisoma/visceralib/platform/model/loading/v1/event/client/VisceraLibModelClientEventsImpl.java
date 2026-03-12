package net.electrisoma.visceralib.platform.model.loading.v1.event.client;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.VisceralUnbakedGeometry;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.IVisceralGeometryContext;
import net.electrisoma.visceralib.event.model.loading.v1.client.VisceralModelEvent;
import net.electrisoma.visceralib.event.model.loading.v1.client.VisceralModelEventHandlers;
import net.electrisoma.visceralib.platform.model.loading.v1.service.event.client.VisceraLibModelEvents;

import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;

import com.google.auto.service.AutoService;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

@AutoService(VisceraLibModelEvents.class)
public final class VisceraLibModelClientEventsImpl implements VisceraLibModelEvents, IEventBusHelper {

	private static boolean initialized = false;

	private static void ensureInitialized(IEventBusHelper helper) {
		if (initialized) return;
		initialized = true;

		helper.withModBus(bus -> {
			bus.addListener((ModelEvent.RegisterGeometryLoaders event) -> {
				VisceralModelEventHandlers.fireRegisterGeometryLoaders(
						new VisceralModelEvent.RegisterGeometryLoaders((id, visceralLoader) -> {
							event.register(id, new IGeometryLoader<VisceralBridge>() {

								@Override
								public @NotNull VisceralBridge read(@NotNull JsonObject json, @NotNull JsonDeserializationContext ctx) {
									return new VisceralBridge(visceralLoader.read(json));
								}
							});
						})
				);
			});

			bus.addListener((ModelEvent.RegisterAdditional event) -> {
				VisceralModelEventHandlers.fireRegisterAdditional(
						new VisceralModelEvent.RegisterAdditional(obj -> {
							if (obj instanceof ModelResourceLocation mrl) {
								event.register(mrl);
							} else if (obj instanceof ResourceLocation rl) {
								event.register(ModelResourceLocation.standalone(rl));
							}
						})
				);
			});

			bus.addListener((ModelEvent.ModifyBakingResult event) -> {
				VisceralModelEventHandlers.fireModifyBakingResult(
						new VisceralModelEvent.ModifyBakingResult(event.getModels())
				);
			});
		});
	}

	@Override
	public void onModifyBakingResult(Consumer<VisceralModelEvent.ModifyBakingResult> handler) {
		VisceralModelEventHandlers.addBakingHandler(handler);
		ensureInitialized(this);
	}

	@Override
	public void onRegisterAdditional(Consumer<VisceralModelEvent.RegisterAdditional> handler) {
		VisceralModelEventHandlers.addAdditionalHandler(handler);
		ensureInitialized(this);
	}

	@Override
	public void onRegisterGeometryLoaders(Consumer<VisceralModelEvent.RegisterGeometryLoaders> handler) {
		VisceralModelEventHandlers.addGeometryHandler(handler);
		ensureInitialized(this);
	}

	private record VisceralBridge(VisceralUnbakedGeometry commonGeom) implements IUnbakedGeometry<VisceralBridge> {

		@Override
		public @NotNull BakedModel bake(
				@NotNull IGeometryBakingContext context,
				@NotNull ModelBaker baker,
				@NotNull Function<Material, TextureAtlasSprite> getter,
				@NotNull ModelState state,
				@NotNull ItemOverrides overrides
		) {
			IVisceralGeometryContext visceralContext = new IVisceralGeometryContext() {

				@Override
				public Material getMaterial(String name) {
					return context.getMaterial(name);
				}

				@Override
				public boolean useAmbientOcclusion() {
					return context.useAmbientOcclusion();
				}

				@Override
				public ItemTransforms getTransforms() {
					return context.getTransforms();
				}

				@Override
				public boolean isGui3d() {
					return context.isGui3d();
				}
			};

			return commonGeom.bake(visceralContext, baker, getter, state);
		}
	}
}
