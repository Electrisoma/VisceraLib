package net.electrisoma.visceralib.impl.modelloader;

import net.electrisoma.visceralib.api.core.PlatformConstants;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

public class Entrypoint {

    public static void init() {

        if (IPlatformHelper.INSTANCE.isDev()) {
            Constants.LOG.info("{} {}: v{}",
                    Constants.NAME, PlatformConstants.getLoader(), PlatformConstants.getVersion()
            );

            if (IEnvHelper.INSTANCE.isCurrent(IEnvHelper.EnvironmentEnum.SERVER)) {
                Constants.LOG.info("{} Server Initialized!", Constants.NAME);
            }
        }
    }
}
