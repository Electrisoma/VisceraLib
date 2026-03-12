package net.electrisoma.visceralib.platform.model.loading.v1.service.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IBakedModelHelper {

	IBakedModelHelper INSTANCE = ServiceHelper.load(IBakedModelHelper.class);

	BakedModel wrap(BakedModel original);

	BakedModel applyTransform(BakedModel model, ItemDisplayContext context, PoseStack stack, boolean leftHand);
}
