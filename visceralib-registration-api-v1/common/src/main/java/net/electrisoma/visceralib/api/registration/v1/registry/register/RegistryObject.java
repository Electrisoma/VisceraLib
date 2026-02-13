package net.electrisoma.visceralib.api.registration.v1.registry.register;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;

/**
 * A platform-agnostic handle for a registered object.
 * <p>
 * This class extends Holder.Reference to integrate directly with vanilla Minecraft
 * systems while providing a simplified #get() method for easy access to the value.
 * <p>
 * On some platforms, the value may not be available immediately after creation. Calling
 * #get() before the registration lifecycle has completed may result in an
 * IllegalStateException or return a default value, depending on the registry's state.
 *
 * @param <T> the type of the registered object.
 */
public class RegistryObject<T> extends Holder.Reference<T> {

	/**
	 * Constructs a new standalone RegistryObject.
	 *
	 * @param owner the HolderOwner; usually the Registry responsible for this holder.
	 * @param key   the ResourceKey identifying the entry in the registry.
	 */
	public RegistryObject(HolderOwner<T> owner, ResourceKey<T> key) {
		super(Type.STAND_ALONE, owner, key, null);
	}

	/**
	 * Retrieves the registered value.
	 * <p>
	 * This is shorthand for #value().
	 * <p>
	 * Note that this will throw an exception
	 * if the value has not been bound yet.
	 *
	 * @return the registered object.
	 * @throws IllegalStateException if the value is not yet bound.
	 */
	public T get() {
		return value();
	}
}
