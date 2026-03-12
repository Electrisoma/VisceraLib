package net.electrisoma.visceralib.platform.model.loading.v1.client;

import net.electrisoma.visceralib.platform.model.loading.v1.service.client.IBakedModelHelper;

import net.neoforged.neoforge.client.model.BakedModelWrapper;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;

import com.mojang.blaze3d.vertex.PoseStack;

import com.google.auto.service.AutoService;

@AutoService(IBakedModelHelper.class)
public class BakedModelHelperImpl implements IBakedModelHelper {

	@Override
	public BakedModel wrap(BakedModel original) {
		return new BakedModelWrapper<>(original) {};
	}

	@Override
	public BakedModel applyTransform(BakedModel model, ItemDisplayContext context, PoseStack stack, boolean leftHand) {
		return model.applyTransform(context, stack, leftHand);
	}
}
