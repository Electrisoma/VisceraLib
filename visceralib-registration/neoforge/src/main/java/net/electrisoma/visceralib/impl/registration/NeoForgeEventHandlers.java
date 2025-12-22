//package net.electrisoma.visceralib.impl.registration;
//
//import net.electrisoma.visceralib.event.core.VisceralEventBus;
//import net.electrisoma.visceralib.event.registration.client.ParticleRegistrationEvent;
//import net.electrisoma.visceralib.platform.registration.ParticleRegistrationImpl;
//import net.electrisoma.visceralib.platform.registration.services.IParticleRegistrationHelper;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
//
//@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
//public class NeoForgeEventHandlers {
//
//    @SubscribeEvent
//    public static void onParticleRegistration(RegisterParticleProvidersEvent event) {
//        IParticleRegistrationHelper helper = ParticleRegistrationImpl.getInstance(event);
//        VisceralEventBus.post(new ParticleRegistrationEvent(helper));
//    }
//}