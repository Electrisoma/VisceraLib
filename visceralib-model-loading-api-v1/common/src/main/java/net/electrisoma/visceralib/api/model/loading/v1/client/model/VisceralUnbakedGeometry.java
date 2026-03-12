package net.electrisoma.visceralib.api.model.loading.v1.client.model;

import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.IVisceralGeometryContext;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public interface VisceralUnbakedGeometry {

	BakedModel bake(IVisceralGeometryContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state);

	default Collection<Material> getMaterials(IVisceralGeometryContext context) {
		return Collections.emptyList();
	}
}
