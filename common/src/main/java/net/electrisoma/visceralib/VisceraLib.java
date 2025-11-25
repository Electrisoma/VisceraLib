package net.electrisoma.visceralib;

import net.electrisoma.visceralib.platform.services.IPlatformHelper;
import net.electrisoma.visceralib.registry.test.TestBlockEntityRegistry;
import net.electrisoma.visceralib.registry.test.TestRegistry;

public class VisceraLib {

    public static void init() {
        TestRegistry.init();
        TestBlockEntityRegistry.init();

        if (IPlatformHelper.INSTANCE.getEnvironmentName().equals("development")) {
            Constants.LOGGER.info("{} {}: v{}", Constants.NAME, Constants.LOADER, Constants.VERSION);
        }
    }
}
