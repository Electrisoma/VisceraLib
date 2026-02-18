package net.electrisoma.visceralib.platform.renderer.v1.services.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.rendering.v1.client.RendererRegistrationEvents;

import java.util.function.Consumer;

public interface VisceraLibRenderingClientEvents {

    VisceraLibRenderingClientEvents INSTANCE = ServiceHelper.load(VisceraLibRenderingClientEvents.class);

    void registerBlockEntityRenderers(Consumer<RendererRegistrationEvents.BlockEntityRenderer> consumer);
    void registerEntityRenderers(Consumer<RendererRegistrationEvents.EntityRenderer> consumer);
    void registerItemRenderers(Consumer<RendererRegistrationEvents.ItemRenderer> consumer);
}
