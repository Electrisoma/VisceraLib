package net.electrisoma.visceralib.api.registration.v1.registry.register;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.mixin.registration.v1.accessor.Holder$ReferenceAccessor;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.registration.v1.services.event.common.VisceraLibRegistrationEvents;

import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The central coordinator for all mod registration.
 * <p>
 * This record maintains a thread-safe multimap of all registered entries, ensuring that
 * no duplicate IDs are registered and providing a bridge between platform-independent
 * code and platform-specific registration events.
 * <p>
 * NeoForge uses a read-only view of all registration entries, while Fabric takes a direct call to Registry#register.
 */
public record VisceralRegistry(String modId) {

	private static final Logger LOG = LoggerFactory.getLogger("VisceralRegistry");

	/** Internal store of all registered entries, keyed by the Registry they belong to. */
	private static final Multimap<ResourceKey<?>, Registration<?, ?>> ENTRIES =
			Multimaps.newMultimap(new ConcurrentHashMap<>(), ConcurrentLinkedQueue::new);

	/** A read-only view of all registration entries for external inspection or platform iteration. */
	public static final Multimap<ResourceKey<?>, Registration<?, ?>> ENTRIES_VIEW =
			Multimaps.unmodifiableMultimap(ENTRIES);

	/**
	 * The primary entry-point for registering any object.
	 * <p>
	 * On Fabric, registration occurs immediately. On other platforms, the registration
	 * is queued in the ENTRIES map to be processed by the platform's specific lifecycle.
	 *
	 * @param <R>      the base type of the registry.
	 * @param <T>      the type of the object being registered.
	 * @param registry the vanilla registry to target.
	 * @param name     the registry name (without namespace).
	 * @param supplier the provider for the object.
	 * @return a RegistryObject which will eventually contain the registered value.
	 */
	public <R, T extends R> RegistryObject<T> register(
			Registry<R> registry,
			String name,
			Supplier<T> supplier
	) {
		ResourceLocation id = RLUtils.path(modId, name);
		return this.internalRegister(registry, registry.key(), id, supplier);
	}

	/**
	 * Helper method to capture registry wildcards and register type-safe holders.
	 */
	private <R, T extends R> RegistryObject<T> internalRegister(
			Registry<R> registry,
			ResourceKey<? extends Registry<R>> registryKey,
			ResourceLocation id,
			Supplier<T> supplier
	) {
		ResourceKey<T> key = ResourceKey.create(ResourceKey.createRegistryKey(registryKey.location()), id);

		//noinspection unchecked
		HolderOwner<T> owner = (HolderOwner<T>) registry.asLookup();

		RegistryObject<T> holder = new RegistryObject<>(owner, key);
		Registration<R, T> registration = new Registration<>(id, registry, supplier, holder);

		var existing = ENTRIES.get(registry.key()).stream()
				.filter(r -> r.id().equals(id))
				.findFirst();

		if (existing.isPresent()) {
			LOG.error("Duplicate registration for ID '{}'. Returning original holder.", id);
			//noinspection unchecked
			return (RegistryObject<T>) existing.get().holder();
		}

		ENTRIES.put(registry.key(), registration);

		// Fabric registers immediately while NeoForge waits for the RegisterEvent.
		if (IPlatformHelper.INSTANCE.isCurrent(IPlatformHelper.PlatformEnum.FABRIC))
			registration.register();

		return holder;
	}

	/** Creates a new static registry. */
	public <T> void newStaticRegistry(
			ResourceKey<Registry<T>> key,
			VisceralRegistrySettings settings,
			Consumer<Registry<T>> onCreated
	) {
		VisceraLibRegistrationEvents.INSTANCE.registerStaticRegistries(registrar -> {
			Registry<T> registry = registrar.register(key, settings);
			onCreated.accept(registry);
		});
	}

	/** Creates a new dynamic registry */
	public <T> void newDynamicRegistry(
			ResourceKey<Registry<T>> key,
			Codec<T> codec,
			@Nullable Codec<T> networkCodec
	) {
		VisceraLibRegistrationEvents.INSTANCE.registerDynamicRegistries(registrar ->
			registrar.register(key, codec, networkCodec)
		);
	}

	/**
	 * Appends a task to be executed immediately after an entry is bound to the vanilla registry.
	 * <p>
	 * If the entry is already registered, the task is executed immediately.
	 *
	 * @param registryKey the key of the registry containing the entry.
	 * @param id          the full ResourceLocation of the entry.
	 * @param task        the logic to execute post-registration.
	 */
	public void addPostRegisterCallback(
			ResourceKey<?> registryKey,
			ResourceLocation id,
			Runnable task
	) {
		ENTRIES.get(registryKey).stream()
				.filter(r -> r.id().equals(id))
				.findFirst()
				.ifPresent(reg -> {
					if (reg.registered().get()) {
						task.run();
					} else {
						reg.afterRegisterHooks().add(task);
					}
				});
	}

	/**
	 * Represents a single registration task and its associated lifecycle.
	 *
	 * @param <R>                 the registry base type.
	 * @param <T>                 the entry type.
	 * @param id                  the full ResourceLocation of the entry.
	 * @param registry            the target registry instance.
	 * @param value               the object supplier.
	 * @param holder              the RegistryObject that will hold the bound value.
	 * @param registered          atomic flag to ensure registration only happens once.
	 * @param afterRegisterHooks  a list of tasks to execute once the object is bound.
	 */
	public record Registration<R, T extends R>(
			ResourceLocation id,
			Registry<R> registry,
			Supplier<T> value,
			RegistryObject<T> holder,
			AtomicBoolean registered,
			List<Runnable> afterRegisterHooks
	) {
		public Registration(
				ResourceLocation id,
				Registry<R> registry,
				Supplier<T> value,
				RegistryObject<T> holder
		) {
			this(id, registry, value, holder, new AtomicBoolean(false), new ArrayList<>());
		}

		/**
		 * Invokes vanilla registration and binds the resulting value to the RegistryObject.
		 * <p>
		 * After binding, all tasks in afterRegisterHooks are executed in the order they were added.
		 */
		public void register() {
			if (registered.compareAndSet(false, true)) {
				T registeredValue = Registry.register(registry, id, value.get());
				//noinspection unchecked
				((Holder$ReferenceAccessor<T>) holder).viscera$bindValue(registeredValue);
				afterRegisterHooks.forEach(Runnable::run);
			}
		}
	}
}
