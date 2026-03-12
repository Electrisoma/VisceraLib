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

public class VisceralGeometryContext implements IVisceralGeometryContext {

	private final JsonObject json;
	private final ModelBaker baker;

	public VisceralGeometryContext(JsonObject json, ModelBaker baker) {
		this.json = json;
		this.baker = baker;
	}

	@Override
	public Material getMaterial(String name) {
		String reference = name;

		if (name.startsWith("#")) {
			String key = name.substring(1);
			JsonObject textures = json.getAsJsonObject("textures");

			if (textures != null && textures.has(key)) {
				reference = textures.get(key).getAsString();
			} else {
				return new Material(InventoryMenu.BLOCK_ATLAS, RLUtils.mc("missingno"));
			}
		}

		return new Material(InventoryMenu.BLOCK_ATLAS, RLUtils.parse(reference));
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
}
