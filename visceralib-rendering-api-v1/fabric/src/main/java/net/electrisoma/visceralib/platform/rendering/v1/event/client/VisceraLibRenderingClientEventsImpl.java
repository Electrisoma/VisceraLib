package net.electrisoma.visceralib.platform.rendering.v1.event.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.event.rendering.v1.client.RendererRegistrationEvents;
import net.electrisoma.visceralib.platform.renderer.v1.services.event.client.VisceraLibRenderingClientEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import java.util.function.Consumer;

@AutoService(VisceraLibRenderingClientEvents.class)
public final class VisceraLibRenderingClientEventsImpl implements VisceraLibRenderingClientEvents {

    @Override
    public void registerBlockEntityRenderers(Consumer<RendererRegistrationEvents.BlockEntityRenderer> consumer) {
        consumer.accept(BlockEntityRenderers::register);
    }

    @Override
    public void registerEntityRenderers(Consumer<RendererRegistrationEvents.EntityRenderer> consumer) {
        consumer.accept(EntityRendererRegistry::register);
    }

    @Override
    public void registerItemRenderers(Consumer<RendererRegistrationEvents.ItemRenderer> consumer) {
        consumer.accept((item, renderer) ->
                BuiltinItemRendererRegistry.INSTANCE.register(item, renderer::renderByItem));
    }
}
