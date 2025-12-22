//package net.electrisoma.visceralib.event.core;
//
//import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Consumer;
//
//public class VisceralEventBus {
//
//    private static boolean clientInitialized = false;
//    private static final Map<Class<? extends VisceralEvent>, List<Consumer<? extends VisceralEvent>>>
//            LISTENERS = new HashMap<>();
//
//    public static void initClient() {
//        clientInitialized = true;
//    }
//
//    public static <T extends VisceralEvent> void subscribe(Class<T> eventClass, Consumer<T> listener) {
//        if (VisceralEvent.ClientSide.class.isAssignableFrom(eventClass)) {
//            if (IEnvHelper.INSTANCE.isCurrent(IEnvHelper.EnvironmentEnum.SERVER))
//                return;
//        }
//        LISTENERS.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(listener);
//    }
//
//    public static <T extends VisceralEvent> void post(T event) {
//        if (event instanceof VisceralEvent.ClientSide && !clientInitialized) return;
//        List<Consumer<? extends VisceralEvent>> list = LISTENERS.get(event.getClass());
//        if (list == null) return;
//        for (Consumer<? extends VisceralEvent> l : list) {
//            ((Consumer<T>) l).accept(event);
//        }
//    }
//}
