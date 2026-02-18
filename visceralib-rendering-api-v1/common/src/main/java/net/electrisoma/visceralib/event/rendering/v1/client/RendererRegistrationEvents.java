package net.electrisoma.visceralib.event.rendering.v1.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class RendererRegistrationEvents {

	@FunctionalInterface
	public interface BlockEntityRenderer {

		<T extends BlockEntity> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> provider);
	}

	@FunctionalInterface
	public interface EntityRenderer {

		<T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
	}

	@FunctionalInterface
	public interface EntityLayer {

		<T extends LivingEntity, M extends EntityModel<T>> void register(RenderLayer<T, M> layer);
	}

	@FunctionalInterface
	public interface ItemRenderer {

		void register(Item item, BlockEntityWithoutLevelRenderer renderer);
	}
}
