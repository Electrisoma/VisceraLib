package net.electrisoma.visceralib.event.core.common;

import java.util.ServiceLoader;

public class VisceralEvents {

    private static final ServiceLoader<IVisceralListener> LISTENERS =
            ServiceLoader.load(IVisceralListener.class);

    public static void registerAll() {
        for (IVisceralListener listener : LISTENERS) {
            listener.register();
        }
    }
}