package net.electrisoma.visceralib.api.registration.v1.registry.helper;

import net.electrisoma.visceralib.api.registration.v1.registry.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.VisceralRegistry;

import net.minecraft.core.Registry;

import java.util.function.Supplier;

/**
 * A standard implementation of {@link AbstractRegistryHelper}.
 * <p>
 * This class provides a concrete way to access the registration methods defined
 * in {@link AbstractRegistryHelper} by wrapping a {@link VisceralRegistry}.
 * <p>
 * It is intended for mods that do not require fancy logic and wish
 * to use the library's shorthand registration methods directly.
 */
public final class VisceralRegistryHelper extends AbstractRegistryHelper {

	/**
	 * Constructs a helper using an existing {@link VisceralRegistry}.
	 *
	 * @param registry The registry handler to wrap.
	 */
	public VisceralRegistryHelper(VisceralRegistry registry) {
		super(registry);
	}

	/**
	 * Creates a new helper and an underlying {@link VisceralRegistry} for the specified Mod ID.
	 *
	 * @param modId The namespace for all objects registered via this helper.
	 * @return A new registry helper instance.
	 */
	public static VisceralRegistryHelper create(String modId) {
		return new VisceralRegistryHelper(new VisceralRegistry(modId));
	}

	/**
	 * Registers an entry into the specified {@link Registry}.
	 *
	 * @param reg      The target {@link Registry}.
	 * @param name     The registry name for the object.
	 * @param supplier A supplier returning the object instance.
	 * @return A {@link RegistryObject} wrapping the entry.
	 */
	@Override
	public <R, T extends R> RegistryObject<T> register(
			Registry<R> reg,
			String name,
			Supplier<T> supplier
	) {
		return super.register(reg, name, supplier);
	}
}
