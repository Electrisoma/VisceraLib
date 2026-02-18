package net.electrisoma.visceralib.platform.rendering.v1.event.client;

import net.electrisoma.visceralib.event.rendering.v1.client.ColorHandlerEvents;
import net.electrisoma.visceralib.event.rendering.v1.client.LayerRegistrationConsumer;
import net.electrisoma.visceralib.event.rendering.v1.client.ModelRegistrationEvents;
import net.electrisoma.visceralib.event.rendering.v1.client.RendererRegistrationEvents;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.rendering.v1.services.event.client.VisceraLibRenderingClientEvents;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AutoService(VisceraLibRenderingClientEvents.class)
public final class VisceraLibRenderingClientEventsImpl implements VisceraLibRenderingClientEvents {

	IEventBus modBus = IPlatformHelper.INSTANCE.getModEventBus();

	@Override
	public void registerBlockEntityRenderers(Consumer<RendererRegistrationEvents.BlockEntityRenderer> consumer) {
		withModBus(bus -> bus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
				consumer.accept(event::registerBlockEntityRenderer)));
	}

	@Override
	public void registerEntityRenderers(Consumer<RendererRegistrationEvents.EntityRenderer> consumer) {
		withModBus(bus -> bus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
				consumer.accept(event::registerEntityRenderer)));
	}

	@Override
	public void registerItemRenderers(Consumer<RendererRegistrationEvents.ItemRenderer> consumer) {
		withModBus(bus -> bus.addListener((RegisterClientExtensionsEvent event) ->
				consumer.accept((item, renderer) ->
						event.registerItem(new IClientItemExtensions() {

							@Override
							public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
								return renderer;
							}
						}, item)
				)));
	}

	@Override
	public void registerModelLayerDefinitions(Consumer<ModelRegistrationEvents.ModelLayer> consumer) {
		withModBus(bus -> bus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
				consumer.accept(event::registerLayerDefinition)));
	}

	@Override
	public void registerEntityLayers(LayerRegistrationConsumer consumer) {
		withModBus(bus -> bus.addListener((EntityRenderersEvent.AddLayers event) -> {
			EntityRendererProvider.Context context = event.getContext();

			for (EntityType<?> type : event.getEntityTypes()) {
				EntityRenderer<?> renderer = event.getRenderer(type);
				if (renderer instanceof LivingEntityRenderer<?, ?> living)
					processLayer(consumer, (EntityType<? extends LivingEntity>) type, living, context);
			}

			for (PlayerSkin.Model model : event.getSkins()) {
				LivingEntityRenderer<? extends Player, ?> playerRenderer = event.getSkin(model);
				if (playerRenderer != null)
					processLayer(consumer, EntityType.PLAYER, playerRenderer, context);
			}
		}));
	}

	@Override
	public void registerItemColorHandlers(Consumer<ColorHandlerEvents.ItemColorHandler> consumer) {
		withModBus(bus -> bus.addListener((RegisterColorHandlersEvent.Item event) ->
				consumer.accept(event::register)));
	}

	@Override
	public void registerBlockColorHandlers(Consumer<ColorHandlerEvents.BlockColorHandler> consumer) {
		withModBus(bus -> bus.addListener((RegisterColorHandlersEvent.Block event) ->
				consumer.accept(event::register)));
	}

	private void withModBus(Consumer<IEventBus> action) {
		if (modBus != null) action.accept(modBus);
	}

	@SuppressWarnings("unchecked")
	private <T extends LivingEntity, M extends EntityModel<T>> void processLayer(
			LayerRegistrationConsumer consumer,
			EntityType<? extends LivingEntity> type,
			LivingEntityRenderer<T, M> renderer,
			EntityRendererProvider.Context context
	) {
		consumer.accept(type, renderer, new RendererRegistrationEvents.EntityLayer() {

			@Override
			public <L extends LivingEntity, ME extends EntityModel<L>> void register(RenderLayer<L, ME> layer) {
				renderer.addLayer((RenderLayer<T, M>) layer);
			}}, context
		);
	}
}
