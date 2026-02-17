package net.electrisoma.visceralib.api.core.debug;

import net.electrisoma.visceralib.api.core.utils.TextUtils;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Utility for printing standardized initialization messages during mod entrypoints.
 * These messages are typically restricted to development environments to keep production logs clean.
 */
public class EntrypointMessages {

	private static final Set<Logger> LOGGERS =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	private EntrypointMessages() {}

	/**
	 * Prints a common initialization message.
	 * If on a dedicated server, appends a server-specific initialization confirmation.
	 *
	 * @param logger the logger to use for output.
	 * @param id     the mod ID to look up version info.
	 * @param name   the human-readable name of the mod.
	 */
	public static void onCommon(Logger logger, String id, String name) {
		if (!IPlatformHelper.INSTANCE.isDev()) return;

		printVersionHeader(logger, id, name);

		IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.SERVER,
				() -> logger.info("{} Server Initialized!", name)
		);
	}

	/**
	 * Prints a client-specific initialization message.
	 *
	 * @param logger the logger to use for output.
	 * @param id     the mod ID to look up version info.
	 * @param name   the human-readable name of the mod.
	 */
	public static void onClient(Logger logger, String id, String name) {
		if (!IPlatformHelper.INSTANCE.isDev()) return;

		printVersionHeader(logger, id, name);

		IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT,
				() -> logger.info("{} Client Initialized!", name)
		);
	}

	/**
	 * Prints the mod's version, platform, and Minecraft version.
	 * This will only print once per unique Logger instance.
	 */
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
