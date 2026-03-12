package net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry;

import net.electrisoma.visceralib.api.core.resources.RLUtils;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import com.google.gson.JsonObject;

import java.util.function.Function;

public class VisceralGeometryContext implements IVisceralGeometryContext {

	private final JsonObject json;
	private final ModelBaker baker;
	private final Function<ResourceLocation, UnbakedModel> resolver;

	public VisceralGeometryContext(JsonObject json, ModelBaker baker) {
		this(json, baker, null);
	}

	public VisceralGeometryContext(JsonObject json, Function<ResourceLocation, UnbakedModel> resolver) {
		this(json, null, resolver);
	}

	private VisceralGeometryContext(JsonObject json, ModelBaker baker, Function<ResourceLocation, UnbakedModel> resolver) {
		this.json = json;
		this.baker = baker;
		this.resolver = resolver;
	}

	@Override
	public Material getMaterial(String name) {
		return resolveMaterialRecursive(this.json, name);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return !json.has("ambientocclusion") || json.get("ambientocclusion").getAsBoolean();
	}

	@Override
	public boolean isGui3d() {
		return !json.has("gui3d") || json.get("gui3d").getAsBoolean();
	}

	@Override
	public ItemTransforms getTransforms() {
		if (json.has("parent")) {
			ResourceLocation parentLoc = RLUtils.parse(json.get("parent").getAsString());
			UnbakedModel parentModel = baker.getModel(parentLoc);
			if (parentModel instanceof BlockModel blockModel)
				return blockModel.getTransforms();
		}
		return ItemTransforms.NO_TRANSFORMS;
	}

	private Material resolveMaterialRecursive(JsonObject currentJson, String name) {
		if (!name.startsWith("#")) {
			return new Material(InventoryMenu.BLOCK_ATLAS, RLUtils.parse(name));
		}

		String key = name.substring(1);
		JsonObject textures = currentJson.getAsJsonObject("textures");

		if (textures != null && textures.has(key)) {
			String ref = textures.get(key).getAsString();
			if (ref.startsWith("#")) return resolveMaterialRecursive(currentJson, ref);
			return new Material(InventoryMenu.BLOCK_ATLAS, RLUtils.parse(ref));
		}

		if (currentJson.has("parent")) {
			ResourceLocation parentLoc = RLUtils.parse(currentJson.get("parent").getAsString());

			UnbakedModel parentModel = null;
			if (baker != null) parentModel = baker.getModel(parentLoc);
			else if (resolver != null) parentModel = resolver.apply(parentLoc);

			if (parentModel instanceof BlockModel bm) {
				return bm.getMaterial(key);
			}
		}

		return new Material(InventoryMenu.BLOCK_ATLAS, RLUtils.mc("missingno"));
	}
}
