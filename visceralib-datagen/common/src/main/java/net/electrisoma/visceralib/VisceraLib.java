package net.electrisoma.visceralib;

import net.electrisoma.visceralib.platform.services.IPlatformHelper;

public class VisceraLib {

    public static void init() {

        if (IPlatformHelper.INSTANCE.getEnvironmentName().equals("development")) {
            Constants.LOGGER.info("{} {}: v{}", Constants.NAME, Constants.LOADER, Constants.VERSION);
        }
    }
}
