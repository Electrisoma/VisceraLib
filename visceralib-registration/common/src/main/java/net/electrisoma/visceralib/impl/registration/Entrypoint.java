package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.core.PlatformConstants;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.api.registration.registry.test.TestBlockEntities;
import net.electrisoma.visceralib.api.registration.registry.test.TestBlocks;
import net.electrisoma.visceralib.api.registration.registry.test.TestItems;
import net.electrisoma.visceralib.api.registration.registry.test.TestTabs;

public class Entrypoint {

    public static void init() {

        TestTabs.init();
//        TestComponents.init();
        TestBlockEntities.init();
        TestBlocks.init();
        TestItems.init();

        if (IPlatformHelper.INSTANCE.isDev()) {
            Constants.LOGGER.info("{} {}: v{}",
                    Constants.NAME, PlatformConstants.getLoader(), PlatformConstants.getVersion()
            );
        }
    }
}
