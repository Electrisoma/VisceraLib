//package net.electrisoma.visceralib.multiloader.neoforge;
//
//import net.electrisoma.visceralib.VisceraLib;
//import net.electrisoma.visceralib.api.registration.builders.TabBuilder;
//
//import net.minecraft.core.registries.Registries;
//
//public class CreativeTabPlatformImpl {
//    public static void registerTab(TabBuilder<?> builder) {
//        var register = VisceraLib.registrar().deferredRegister(Registries.CREATIVE_MODE_TAB);
//        register.register(builder.getName(), builder::build);
//    }
//}
