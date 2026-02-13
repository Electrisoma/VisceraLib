//package net.electrisoma.visceralib.platform.registration.v1.client;
//
//import net.electrisoma.visceralib.api.registration.v1.client.renderer.BlockEntityRendererRegistrar;
//import net.electrisoma.visceralib.api.registration.v1.client.renderer.EntityRendererRegistrar;
//import net.electrisoma.visceralib.api.registration.v1.client.renderer.ItemRendererRegistrar;
//import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
//import net.electrisoma.visceralib.platform.registration.v1.client.services.IRendererEvents;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.client.event.EntityRenderersEvent;
//
//import java.util.function.Consumer;
//
//public class RendererEventsImpl implements IRendererEvents {

//
//    @Override
//    public void registerEntityRenderers(Consumer<EntityRendererRegistrar> consumer) {
//        IEventBus bus = IPlatformHelper.INSTANCE.getModEventBus();
//        bus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
//            consumer.accept(event::registerEntityRenderer)
//        );
//    }
//
//    @Override
//    public void registerBlockEntityRenderers(Consumer<BlockEntityRendererRegistrar> consumer) {
//        IEventBus bus = IPlatformHelper.INSTANCE.getModEventBus();
//        bus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
//            consumer.accept(event::registerBlockEntityRenderer)
//        );
//    }
//
//    @Override
//    public void registerItemRenderers(Consumer<ItemRendererRegistrar> consumer) {
//    }
//
//}
