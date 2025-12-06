package net.electrisoma.visceralib.platform.core.services;

import net.electrisoma.visceralib.api.core.Services;

public interface IPlatformHelper {

    IPlatformHelper INSTANCE = Services.load(IPlatformHelper.class);

    enum PlatformEnum {
//        FORGE,
        NEOFORGE,
        FABRIC
    }

    PlatformEnum getPlatformInfo();

    String getVersion();

    boolean isModLoaded(String modId);

    boolean isDev();

    default boolean isCurrent(PlatformEnum info) {
        return getPlatformInfo() == info;
    }

    default String getEnvironmentName() {
        return isDev() ? "development" : "production";
    }

    default String getPlatformName() {
        return getPlatformInfo().name().toLowerCase();
    }
}