package net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;

public interface IVisceralGeometryContext {

	Material getMaterial(String name);

	boolean isGui3d();

	boolean useAmbientOcclusion();

	ItemTransforms getTransforms();
}
