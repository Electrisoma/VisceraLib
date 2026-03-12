package net.electrisoma.visceralib.platform.model.loading.v1.client;

import net.electrisoma.visceralib.platform.model.loading.v1.service.client.IBakedModelHelper;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.blaze3d.vertex.PoseStack;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@AutoService(IBakedModelHelper.class)
public class BakedModelHelperImpl implements IBakedModelHelper {

	@Override
	public BakedModel wrap(BakedModel commonModel) {
		return new FabricWrapper(commonModel);
	}

	@Override
	public BakedModel applyTransform(BakedModel model, ItemDisplayContext context, PoseStack stack, boolean leftHand) {
		model.getTransforms().getTransform(context).apply(leftHand, stack);
		return model;
	}

	private record FabricWrapper(BakedModel wrapped) implements BakedModel, FabricBakedModel {

		@Override
		public boolean isVanillaAdapter() {
			return !(wrapped instanceof FabricBakedModel fbm) || fbm.isVanillaAdapter();
		}

		@Override
		public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
			if (wrapped instanceof FabricBakedModel fbm)
				fbm.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		}

		@Override
		public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
			if (wrapped instanceof FabricBakedModel fbm)
				fbm.emitItemQuads(stack, randomSupplier, context);
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
}
