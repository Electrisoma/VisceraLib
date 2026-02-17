package net.electrisoma.visceralib.platform.core.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;

public interface IPlatformHelper {

	IPlatformHelper INSTANCE = ServiceHelper.load(IPlatformHelper.class);

	<T> void registerModBus(T modBus);

	<T> T getModEventBus();

	/**
	 * Supported modding platforms.
	 */
	enum PlatformEnum {
		NEOFORGE,
		FABRIC
	}

	/**
	 * Identifies the active modloader.
	 * @return The {@link PlatformEnum} representing the active loader.
	 */
	PlatformEnum getPlatformInfo();

	/**
	 * Gets the version of the mod.
	 * @return A string representing the version.
	 */
	String getVersion(String id);

	/**
	 * Gets the version of Minecraft currently running.
	 * @return The game version.
	 */
	String getMinecraftVersion();

	/**
	 * Checks if a specific mod is installed and active in the current instance.
	 * @param modId The unique string ID of the mod (e.g., "jei", "sodium").
	 * @return {@code true} if the mod is loaded.
	 */
	boolean isModLoaded(String modId);

	/**
	 * Determines if the game is running in a development environment (IDE)
	 * or a production environment (the user's launcher).
	 * @return {@code true} if running in a dev environment.
	 */
	boolean isDev();

	/**
	 * Checks if the current platform matches the specified loader.
	 * @param info The platform to check against.
	 * @return {@code true} if the current platform matches {@code info}.
	 */
	default boolean isCurrent(PlatformEnum info) {
		return getPlatformInfo() == info;
	}

	/**
	 * Gets a human-readable string for the current development state.
	 * Useful for logging or conditional file loading.
	 * @return "dev" if in an IDE, "prod" otherwise.
	 */
	default String getEnvironmentName() {
		return isDev() ? "dev" : "prod";
	}

	/**
	 * Gets the lowercase name of the current platform.
	 * Commonly used for creating platform-specific folder paths or config keys.
	 * @return The platform name (e.g., "neoforge", "fabric").
	 */
	default String getPlatformName() {
		return getPlatformInfo().name().toLowerCase();
	}
}
