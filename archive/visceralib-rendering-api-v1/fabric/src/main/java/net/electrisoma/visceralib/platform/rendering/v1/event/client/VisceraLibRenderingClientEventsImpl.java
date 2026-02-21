package net.electrisoma.visceralib.platform.rendering.v1.event.client;

import net.electrisoma.visceralib.event.rendering.v1.client.ColorHandlerEvents;
import net.electrisoma.visceralib.event.rendering.v1.client.LayerRegistrationConsumer;
import net.electrisoma.visceralib.event.rendering.v1.client.ModelRegistrationEvents;
import net.electrisoma.visceralib.event.rendering.v1.client.RendererRegistrationEvents;
import net.electrisoma.visceralib.platform.rendering.v1.services.event.client.VisceraLibRenderingClientEvents;

import net.fabricmc.fabric.api.client.rendering.v1.*;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import com.google.auto.service.AutoService;

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

	@Override
	public void registerModelLayerDefinitions(Consumer<ModelRegistrationEvents.ModelLayer> consumer) {
		consumer.accept((location, supplier) ->
				EntityModelLayerRegistry.registerModelLayer(location, supplier::get));
	}

	@Override
	public void registerEntityLayers(LayerRegistrationConsumer consumer) {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((
				entityType,
				entityRenderer,
				registrationHelper,
				context) ->
				consumer.accept(entityType, entityRenderer, registrationHelper::register, context)
		);
	}

	@Override
	public void registerItemColorHandlers(Consumer<ColorHandlerEvents.ItemColorHandler> consumer) {
		consumer.accept(ColorProviderRegistry.ITEM::register);
	}

	@Override
	public void registerBlockColorHandlers(Consumer<ColorHandlerEvents.BlockColorHandler> consumer) {
		consumer.accept(ColorProviderRegistry.BLOCK::register);
	}
}
