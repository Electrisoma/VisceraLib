//package net.electrisoma.visceralib.impl.registration;
//
//import net.electrisoma.visceralib.event.core.VisceralEventBus;
//import net.electrisoma.visceralib.event.registration.client.ParticleRegistrationEvent;
//import net.electrisoma.visceralib.platform.registration.ParticleRegistrationImpl;
//import net.electrisoma.visceralib.platform.registration.services.IParticleRegistrationHelper;
//
//public class FabricEventHandlers {
//
//    public static void onParticleRegistration() {
//        IParticleRegistrationHelper helper = ParticleRegistrationImpl.getInstance();
//        VisceralEventBus.post(new ParticleRegistrationEvent(helper));
//    }
//}