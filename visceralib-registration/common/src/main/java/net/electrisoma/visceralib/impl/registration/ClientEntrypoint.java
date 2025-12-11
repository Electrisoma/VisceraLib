package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

public class ClientEntrypoint {

    public static void init() {

        if (IPlatformHelper.INSTANCE.isDev() && IEnvHelper.INSTANCE.isCurrent(EnvironmentEnum.CLIENT)) {
            Constants.LOG.info("{} Client Initialized!", Constants.NAME);
        }
    }
}
