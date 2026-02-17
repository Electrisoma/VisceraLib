package net.electrisoma.visceralib.platform.core.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;

import java.util.function.Supplier;

public interface IEnvHelper {

	IEnvHelper INSTANCE = ServiceHelper.load(IEnvHelper.class);

	/**
	 * Represents the physical distribution of the game.
	 */
	enum EnvironmentEnum {
		CLIENT,
		SERVER
	}

	/**
	 * Gets the current physical environment.
	 * @return The {@link EnvironmentEnum} representing the current side.
	 */
	EnvironmentEnum getEnvironmentInfo();

	/**
	 * Checks if the current environment matches the {@link EnvironmentEnum}.
	 * @param info The environment to check against.
	 * @return {@code true} if the current environment matches {@code info}.
	 */
	default boolean isCurrent(EnvironmentEnum info) {
		return getEnvironmentInfo() == info;
	}

	/**
	 * Executes a piece of logic only if the game is running on the specified side.
	 * Use this for side-safe logic that doesn't involve direct references to side-only classes.
	 * @param type The environment required to run the code.
	 * @param runnable The logic to execute.
	 */
	default void runIfCurrent(EnvironmentEnum type, Runnable runnable) {
		if (isCurrent(type))
			runnable.run();
	}

	/**
	 * Executes environment-specific logic and returns a value.
	 * <p>
	 * <b>Warning:</b> This method is crucial for avoiding {@link NoClassDefFoundError}.
	 * By using {@link Supplier}, you prevent the JVM from attempting to load classes
	 * inside the suppliers until the specific side is confirmed.
	 * @param <T> The type of the object being returned.
	 * @param clientTarget A supplier for logic/objects only available on the Client.
	 * @param serverTarget A supplier for logic/objects only available on the Server.
	 * @return The result from the supplier corresponding to the current environment.
	 */
	default <T> T unsafeRunForDist(Supplier<T> clientTarget, Supplier<T> serverTarget) {
		return switch (getEnvironmentInfo()) {
			case CLIENT -> clientTarget.get();
			case SERVER -> serverTarget.get();
		};
	}
}
