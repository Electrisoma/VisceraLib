//package net.electrisoma.electrisoma.testmod.neoforge;
//
//import net.electrisoma.testmod.TestMod;
//
//import net.electrisoma.visceralib.api.neoforge.registration.VisceralDeferredRegisterNeoForge;
//import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
//import net.electrisoma.visceralib.api.registration.VisceralRegistries;
//
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.fml.ModLoadingContext;
//import net.neoforged.fml.common.Mod;
//import net.neoforged.neoforge.common.NeoForge;
//
//@Mod(TestMod.MOD_ID)
//public class TestModImpl {
//    static IEventBus eventBus;
//    static IEventBus neoforgeBus;
//
//    public TestModImpl() {
//        eventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
//        neoforgeBus = NeoForge.EVENT_BUS;
//
//        VisceralRegistries.setFactory(VisceralDeferredRegisterNeoForge::new);
//
//        TestMod.init();
//
//        for (VisceralDeferredRegister<?> deferred : VisceralRegistries.getAllForMod(TestMod.MOD_ID))
//            deferred.registerToEventBus(eventBus);
//    }
//
//}
