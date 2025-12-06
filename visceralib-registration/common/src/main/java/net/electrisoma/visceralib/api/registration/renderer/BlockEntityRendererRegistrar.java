package net.electrisoma.visceralib.api.registration.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface BlockEntityRendererRegistrar {
    <T extends BlockEntity> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> renderer);
}
