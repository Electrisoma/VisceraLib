package net.electrisoma.visceralib.event.model.loading.v1.client;

import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.VisceralGeometryLoader;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class VisceralModelEvent {

	private VisceralModelEvent() {}

	public static class ModifyBakingResult extends VisceralModelEvent {

		private final Map<ModelResourceLocation, BakedModel> models;

		public ModifyBakingResult(Map<ModelResourceLocation, BakedModel> models) {
			this.models = models;
		}

		public Map<ModelResourceLocation, BakedModel> getModels() {
			return models;
		}
	}

	public static class RegisterAdditional extends VisceralModelEvent {

		private final Consumer<Object> registerAction;

		public RegisterAdditional(Consumer<Object> registerAction) {
			this.registerAction = registerAction;
		}

		public void register(ResourceLocation model) {
			registerAction.accept(model);
		}

		public void register(ModelResourceLocation model) {
			registerAction.accept(model);
		}
	}

	public static class RegisterGeometryLoaders extends VisceralModelEvent {

		private final BiConsumer<ResourceLocation, VisceralGeometryLoader> registerAction;

		public RegisterGeometryLoaders(BiConsumer<ResourceLocation, VisceralGeometryLoader> registerAction) {
			this.registerAction = registerAction;
		}

		public void register(ResourceLocation id, VisceralGeometryLoader loader) {
			registerAction.accept(id, loader);
		}
	}
}
