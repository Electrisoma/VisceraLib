package net.electrisoma.visceralib.api.registration.v1.registry;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;

import net.minecraft.core.Registry;

import java.util.function.Supplier;

/**
 * A type-safe wrapper for a specific Registry.
 * <p>
 * This allows for concise registration calls where the target registry is implied.
 *
 * @param <T> the base type of the objects contained within the bound registry.
 */
public class BoundRegistryHelper<T> extends AbstractRegistryHelper {

	private final Registry<T> target;

	public BoundRegistryHelper(VisceralRegistry registry, Registry<T> target) {
		super(registry);
		this.target = target;
	}

	/**
	 * Registers an object to the bound registry.
	 *
	 * @param name     the registry path/name.
	 * @param supplier a supplier for the object instance.
	 * @param <V>      the specific subtype being registered.
	 * @return a RegistryObject wrapping the entry.
	 */
	public <V extends T> RegistryObject<V> register(
			String name,
			Supplier<V> supplier
	) {
		return this.registry.register(target, name, supplier);
	}
}
