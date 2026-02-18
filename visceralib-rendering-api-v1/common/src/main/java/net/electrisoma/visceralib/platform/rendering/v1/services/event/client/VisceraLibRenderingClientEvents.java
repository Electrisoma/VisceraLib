package net.electrisoma.visceralib.platform.rendering.v1.services.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.rendering.v1.client.ColorHandlerEvents;
import net.electrisoma.visceralib.event.rendering.v1.client.LayerRegistrationConsumer;
import net.electrisoma.visceralib.event.rendering.v1.client.ModelRegistrationEvents;
import net.electrisoma.visceralib.event.rendering.v1.client.RendererRegistrationEvents;

import java.util.function.Consumer;

public interface VisceraLibRenderingClientEvents {

	VisceraLibRenderingClientEvents INSTANCE = ServiceHelper.load(VisceraLibRenderingClientEvents.class);

	void registerBlockEntityRenderers(Consumer<RendererRegistrationEvents.BlockEntityRenderer> consumer);
	void registerEntityRenderers(Consumer<RendererRegistrationEvents.EntityRenderer> consumer);
	void registerItemRenderers(Consumer<RendererRegistrationEvents.ItemRenderer> consumer);

	void registerModelLayerDefinitions(Consumer<ModelRegistrationEvents.ModelLayer> consumer);

	void registerEntityLayers(LayerRegistrationConsumer consumer);

	void registerItemColorHandlers(Consumer<ColorHandlerEvents.ItemColorHandler> consumer);
	void registerBlockColorHandlers(Consumer<ColorHandlerEvents.BlockColorHandler> consumer);
}
