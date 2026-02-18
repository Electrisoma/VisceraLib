package net.electrisoma.visceralib.event.rendering.v1.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LayerRegistrationConsumer {

    void accept(
            EntityType<? extends LivingEntity> type,
            LivingEntityRenderer<?, ?> renderer,
            RendererRegistrationEvents.EntityLayer registrar,
            EntityRendererProvider.Context context
    );
}