package net.electrisoma.visceralib.api.model.loading.v1.client.obj;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.VisceralBakedModel;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.VisceralUnbakedGeometry;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.IVisceralGeometryContext;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.math.Transformation;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VisceralObjGeometry implements VisceralUnbakedGeometry {

	private final float[] positions;
	private final float[] texCoords;
	private final float[] normals;

	private final Map<String, IntArrayList> materialGroups;
	private final Map<String, ResourceLocation> materialMap;

	private final int tintIndex;

	public VisceralObjGeometry(
			float[] positions,
			float[] texCoords,
			float[] normals,
			Map<String, IntArrayList> materialGroups,
			Map<String, ResourceLocation> materialMap,
			int tintIndex
	) {
		this.positions = positions;
		this.texCoords = texCoords;
		this.normals = normals;
		this.materialGroups = materialGroups;
		this.materialMap = materialMap;
		this.tintIndex = tintIndex;
	}

	@Override
	public BakedModel bake(IVisceralGeometryContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state) {
		Transformation transformation = state.getRotation();
		TextureAtlasSprite particleSprite = spriteGetter.apply(context.getMaterial("#particle"));
		List<BakedQuad> quads = new ArrayList<>();

		for (Map.Entry<String, IntArrayList> entry : materialGroups.entrySet()) {
			IntArrayList indices = entry.getValue();
			TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(getTextureLookup(entry.getKey())));

			for (int i = 0; i < indices.size(); i += 12) {
				quads.add(createQuad(indices, i, sprite, transformation));
			}
		}

		return new VisceralBakedModel(getBakedModel(particleSprite, quads, context)) {};
	}

	@Override
	public Collection<Material> getMaterials(IVisceralGeometryContext context, Function<ResourceLocation, UnbakedModel> modelGetter) {
		List<Material> materials = new ArrayList<>();
		ResourceLocation blockAtlas = RLUtils.mc("textures/atlas/blocks.png");

		for (String matKey : materialGroups.keySet()) {
			String lookup = getTextureLookup(matKey);
			if (!lookup.startsWith("#")) {
				materials.add(new Material(blockAtlas, ResourceLocation.parse(lookup)));
			}
		}
		return materials;
	}

	private BakedQuad createQuad(IntArrayList indices, int offset, TextureAtlasSprite sprite, Transformation transform) {
		int[] vertexData = new int[32];
		Matrix4f matrix = transform.getMatrix();
		Vector3f tempVec = new Vector3f();

		for (int i = 0; i < 4; i++) {
			int baseIdx = offset + (i * 3);
			int vIdx = indices.getInt(baseIdx);
			int tIdx = indices.getInt(baseIdx + 1);
			int nIdx = indices.getInt(baseIdx + 2);

			int vOffset = i * 8;

			// pos
			tempVec.set(positions[vIdx * 3], positions[vIdx * 3 + 1], positions[vIdx * 3 + 2]);
			matrix.transformPosition(tempVec);
			vertexData[vOffset] = Float.floatToRawIntBits(tempVec.x());
			vertexData[vOffset + 1] = Float.floatToRawIntBits(tempVec.y());
			vertexData[vOffset + 2] = Float.floatToRawIntBits(tempVec.z());
			vertexData[vOffset + 3] = -1;

			// uvs
			if (tIdx >= 0) {
				vertexData[vOffset + 4] = Float.floatToRawIntBits(sprite.getU(texCoords[tIdx * 2]));
				vertexData[vOffset + 5] = Float.floatToRawIntBits(sprite.getV(texCoords[tIdx * 2 + 1]));
			}

			// normals
			if (nIdx >= 0) {
				tempVec.set(normals[nIdx * 3], normals[nIdx * 3 + 1], normals[nIdx * 3 + 2]);
				transform.getLeftRotation().transform(tempVec);
				int nx = ((int) (tempVec.x() * 127)) & 0xFF;
				int ny = ((int) (tempVec.y() * 127)) & 0xFF;
				int nz = ((int) (tempVec.z() * 127)) & 0xFF;
				vertexData[vOffset + 7] = nx | (ny << 8) | (nz << 16);
			}
		}

		Direction dir = Direction.getNearest(tempVec.x(), tempVec.y(), tempVec.z());
		return new BakedQuad(vertexData, this.tintIndex, dir, sprite, true);
	}

	private String getTextureLookup(String key) {
		ResourceLocation texRef = materialMap.get(key);
		if (texRef == null) return "#" + key;
		if (texRef.getNamespace().equals("visceral_variable")) return "#" + texRef.getPath();
		return texRef.toString();
	}

	private static @NotNull BakedModel getBakedModel(TextureAtlasSprite particleSprite, List<BakedQuad> unculled, IVisceralGeometryContext context) {
		final TextureAtlasSprite finalParticle = particleSprite;

		return new BakedModel() {

			@Override
			public @NotNull List<BakedQuad> getQuads(BlockState s, Direction d, @NotNull RandomSource r) {
				return d == null ? unculled : List.of();
			}

			@Override
			public boolean useAmbientOcclusion() {
				return context.useAmbientOcclusion();
			}

			@Override
			public boolean isGui3d() {
				return context.isGui3d();
			}

			@Override
			public boolean usesBlockLight() {
				return true;
			}

			@Override
			public boolean isCustomRenderer() {
				return false;
			}

			@Override
			public @NotNull TextureAtlasSprite getParticleIcon() {
				return finalParticle;
			}

			@Override
			public @NotNull ItemTransforms getTransforms() {
				return context.getTransforms();
			}

			@Override
			public @NotNull ItemOverrides getOverrides() {
				return ItemOverrides.EMPTY;
			}
		};
	}
}
