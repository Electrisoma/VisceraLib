package net.electrisoma.visceralib.platform.services;

import net.electrisoma.visceralib.platform.Services;

public interface IPlatformHelper {

    IPlatformHelper INSTANCE = Services.load(IPlatformHelper.class);

    enum PlatformEnum {
        FORGE,
        NEOFORGE,
        FABRIC
    }

    PlatformEnum getPlatformInfo();

    String getVersion();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default boolean isCurrent(PlatformEnum info) {
        return getPlatformInfo() == info;
    }

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    default String getPlatformName() {
        return getPlatformInfo().name().toLowerCase();
    }
}