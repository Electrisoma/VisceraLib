package net.electrisoma.visceralib.api.model.loading.v1.client.model;

import net.electrisoma.visceralib.platform.model.loading.v1.service.client.IBakedModelHelper;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class VisceralBakedModel implements BakedModel {

	protected final BakedModel wrapped;
	private final BakedModel wrapper;

	public VisceralBakedModel(BakedModel wrapped) {
		this.wrapped = wrapped;
		this.wrapper = IBakedModelHelper.INSTANCE.wrap(this);
	}

	public BakedModel getWrappedModel() {
		return wrapped;
	}

	public BakedModel handleTransformation(ItemDisplayContext context, PoseStack stack, boolean leftHand) {
		IBakedModelHelper.INSTANCE.applyTransform(this.wrapped, context, stack, leftHand);
		return wrapper;
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(BlockState s, Direction f, @NotNull RandomSource r) {
		return wrapped.getQuads(s, f, r);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return wrapped.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return wrapped.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return wrapped.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return wrapped.isCustomRenderer();
	}

	@Override
	public @NotNull TextureAtlasSprite getParticleIcon() {
		return wrapped.getParticleIcon();
	}

	@Override
	public @NotNull ItemTransforms getTransforms() {
		return wrapped.getTransforms();
	}

	@Override
	public @NotNull ItemOverrides getOverrides() {
		return wrapped.getOverrides();
	}
}
