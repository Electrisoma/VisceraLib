package net.electrisoma.visceralib.event.rendering.v1.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class RendererRegistrationEvents {

	public interface BlockEntityRenderer {

		<T extends BlockEntity> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> provider);
	}

	public interface EntityRenderer {

		<T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
	}

	public interface ItemRenderer {

		void register(Item item, BlockEntityWithoutLevelRenderer renderer);
	}
}
