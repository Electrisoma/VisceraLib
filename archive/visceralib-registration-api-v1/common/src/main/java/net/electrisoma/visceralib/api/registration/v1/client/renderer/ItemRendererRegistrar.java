package net.electrisoma.visceralib.api.registration.v1.client.renderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemRendererRegistrar {
    void register(Item item, BlockEntityWithoutLevelRenderer renderer);
}
