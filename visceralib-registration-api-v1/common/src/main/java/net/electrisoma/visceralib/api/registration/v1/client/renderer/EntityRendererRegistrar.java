package net.electrisoma.visceralib.api.registration.v1.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface EntityRendererRegistrar {

	<T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> renderer);
}
