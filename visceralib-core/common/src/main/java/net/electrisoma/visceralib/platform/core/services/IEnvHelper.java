package net.electrisoma.visceralib.platform.core.services;

import net.electrisoma.visceralib.api.core.Services;

import java.util.function.Supplier;

public interface IEnvHelper {

    IEnvHelper INSTANCE = Services.load(IEnvHelper.class);

    enum EnvironmentEnum {
        CLIENT,
        SERVER
    }

    EnvironmentEnum getEnvironmentInfo();

    default boolean isCurrent(EnvironmentEnum info) {
        return getEnvironmentInfo() == info;
    }

    default void runIfCurrent(EnvironmentEnum type, Runnable runnable) {
        if (isCurrent(type))
            runnable.run();
    }

    default <T> T unsafeRunForDist(Supplier<T> clientTarget, Supplier<T> serverTarget) {
        return switch (getEnvironmentInfo()) {
            case CLIENT -> clientTarget.get();
            case SERVER -> serverTarget.get();
        };
    }
}
