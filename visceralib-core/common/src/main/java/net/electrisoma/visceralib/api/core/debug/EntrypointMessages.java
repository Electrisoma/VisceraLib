package net.electrisoma.visceralib.api.core.debug;

import net.electrisoma.visceralib.api.core.utils.TextUtils;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class EntrypointMessages {

    private static final Set<Logger> LOGGERS =
            Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    private EntrypointMessages() {}

    public static void onCommon(Logger logger, String id, String name) {
        if (!IPlatformHelper.INSTANCE.isDev()) return;

        printVersionHeader(logger, id, name);

        IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.SERVER,
                () -> logger.info("{} Server Initialized!", name)
        );
    }

    public static void onClient(Logger logger, String id, String name) {
        if (!IPlatformHelper.INSTANCE.isDev()) return;

        printVersionHeader(logger, id, name);

        IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT,
                () -> logger.info("{} Client Initialized!", name)
        );
    }

    private static void printVersionHeader(Logger logger, String id, String name) {
        if (LOGGERS.add(logger)) {
            logger.info("{} {}: v{} for mc{}",
                    name,
                    TextUtils.toSmartPascalCase(IPlatformHelper.INSTANCE.getPlatformName()),
                    IPlatformHelper.INSTANCE.getVersion(id),
                    IPlatformHelper.INSTANCE.getMinecraftVersion()
            );
        }
    }
}
