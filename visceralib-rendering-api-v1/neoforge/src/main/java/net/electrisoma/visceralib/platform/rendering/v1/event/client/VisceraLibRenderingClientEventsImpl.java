package net.electrisoma.visceralib.platform.rendering.v1.event.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.event.rendering.v1.client.RendererRegistrationEvents;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.renderer.v1.services.event.client.VisceraLibRenderingClientEvents;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AutoService(VisceraLibRenderingClientEvents.class)
public final class VisceraLibRenderingClientEventsImpl implements VisceraLibRenderingClientEvents {

    IEventBus modBus = IPlatformHelper.INSTANCE.getModEventBus();

    @Override
    public void registerBlockEntityRenderers(Consumer<RendererRegistrationEvents.BlockEntityRenderer> consumer) {
        if (modBus == null) return;

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
                consumer.accept(event::registerBlockEntityRenderer));
    }

    @Override
    public void registerEntityRenderers(Consumer<RendererRegistrationEvents.EntityRenderer> consumer) {
        if (modBus == null) return;

        modBus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
                consumer.accept(event::registerEntityRenderer));
    }

    @Override
    public void registerItemRenderers(Consumer<RendererRegistrationEvents.ItemRenderer> consumer) {
        if (modBus == null) return;

        modBus.addListener((RegisterClientExtensionsEvent event) ->
                consumer.accept((item, renderer) ->
                        event.registerItem(new IClientItemExtensions() {

                            @Override
                            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                                return renderer;
                            }}, item
                        )
                ));
    }
}
