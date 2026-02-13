package net.electrisoma.visceralib.api.registration.v1.registry;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.api.registration.v1.registry.register.dynamic.DynamicRegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluidProperties;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The Abstract implementation of a registry helper.
 * This can be used to define the workflow of registration.
 * <p>
 * Alternatively, this library provides a pre-made implementation.
 *
 * @see VisceralRegistryHelper
 */
public abstract class AbstractRegistryHelper {

	/** The underlying platform-agnostic registry handler. */
	protected final VisceralRegistry registry;

	protected AbstractRegistryHelper(VisceralRegistry registry) {
		this.registry = registry;
	}

	/** @return The Mod ID associated with this helper. */
	public String modId() {
		return this.registry.modId();
	}

	/**
	 * Internal method to register an entry to a specified registry.
	 *
	 * @param registry the target vanilla registry.
	 * @param name     the registry name of the object.
	 * @param supplier a supplier returning the object to register.
	 * @return a RegistryObject wrapping the registered entry.
	 */
	public <R, T extends R> RegistryObject<T> register(
			Registry<R> registry,
			String name,
			Supplier<T> supplier
	) {
		return this.registry.register(registry, name, supplier);
	}

	// --- Custom Registry Management ---

	/**
	 * Creates a helper focused on a specific registry.
	 * @param target the registry to bind to.
	 */
	public <T> BoundRegistryHelper<T> forRegistry(Registry<T> target) {
		return new BoundRegistryHelper<>(this.registry, target);
	}

	/** Registers a new custom static registry with default settings. */
	public <T> Registry<T> newStaticRegistry(ResourceKey<Registry<T>> key) {
		return this.registry.newStaticRegistry(key, VisceralRegistrySettings.DEFAULT);
	}

	/** Registers a new custom static registry with specific settings. */
	public <T> Registry<T> newStaticRegistry(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings) {
		return this.registry.newStaticRegistry(key, settings);
	}

	/** Registers a new dynamic registry. */
	public <T> DynamicRegistryObject<T> newDynamicRegistry(
			ResourceKey<Registry<T>> key,
			Codec<T> codec
	) {
		return newDynamicRegistry(key, codec, null);
	}

	/** Registers a new dynamic registry with an optional separate network codec. */
	public <T> DynamicRegistryObject<T> newDynamicRegistry(
			ResourceKey<Registry<T>> key,
			Codec<T> codec,
			@Nullable Codec<T> networkCodec
	) {
		this.registry.newDynamicRegistry(key, codec, networkCodec);
		return new DynamicRegistryObject<>(key);
	}

	// --- Blocks and Items ---

	/** Shorthand for registering a Block. */
	public <T extends Block> RegistryObject<T> block(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.BLOCK, name, supplier);
	}

	/** Backport of 1.21.11 block registration using a property factory. */
	public <T extends Block> RegistryObject<T> block(
			String name,
			Function<BlockBehaviour.Properties, ? extends T> factory,
			Supplier<BlockBehaviour.Properties> supplier
	) {
		return register(BuiltInRegistries.BLOCK, name, () -> factory.apply(supplier.get()));
	}

	/** Shorthand for registering an Item. */
	public <T extends Item> RegistryObject<T> item(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ITEM, name, supplier);
	}

	/** Backport of 1.21.11 item registration using a property factory. */
	public <T extends Item> RegistryObject<T> item(
			String name,
			Function<Item.Properties, ? extends T> factory,
			Supplier<Item.Properties> supplier
	) {
		return register(BuiltInRegistries.ITEM, name, () -> factory.apply(supplier.get()));
	}

	/** Registers a block and automatically creates a corresponding BlockItem. */
	public <T extends Block> RegistryObject<T> blockWithItem(String name, Supplier<T> blockSupplier) {
		RegistryObject<T> blockObj = block(name, blockSupplier);
		this.registry.addPostRegisterCallback(BuiltInRegistries.BLOCK.key(), blockObj.key().location(), () ->
			item(name, () -> new BlockItem(blockObj.get(), new Item.Properties()))
		);
		return blockObj;
	}

	/** Backport of 1.21.11 block item registration. */
	public <T extends Block> RegistryObject<T> blockWithItem(
			String name,
			Function<BlockBehaviour.Properties, ? extends T> factory,
			Supplier<BlockBehaviour.Properties> supplier
	) {
		RegistryObject<T> blockObj = block(name, () -> factory.apply(supplier.get()));
		this.registry.addPostRegisterCallback(BuiltInRegistries.BLOCK.key(), blockObj.key().location(), () ->
			item(name, p -> new BlockItem(blockObj.get(), p), Item.Properties::new)
		);
		return blockObj;
	}

	// --- Gameplay Mechanics ---

	/** Shorthand for registering a Block Entity Type. */
	public <T extends BlockEntityType<?>> RegistryObject<T> blockEntityType(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, supplier);
	}

	/** Backport of 1.21.11 Block Entity registration. */
	public <T extends BlockEntityType<?>> RegistryObject<T> blockEntityType(
			String name,
			Function<BlockEntityType.BlockEntitySupplier<?>, ? extends T> factory,
			Supplier<BlockEntityType.BlockEntitySupplier<?>> supplier
	) {
		return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, () -> factory.apply(supplier.get()));
	}

	/** Shorthand for registering an Entity Type. */
	public <T extends EntityType<?>> RegistryObject<T> entityType(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ENTITY_TYPE, name, supplier);
	}

	/** Backport of 1.21.11 Entity registration. */
	public <T extends EntityType<?>> RegistryObject<T> entityType(
			String name,
			Function<EntityType.EntityFactory<?>, ? extends T> factory,
			Supplier<EntityType.EntityFactory<?>> supplier
	) {
		return register(BuiltInRegistries.ENTITY_TYPE, name, () -> factory.apply(supplier.get()));
	}

	/** Shorthand for registering a Fluid. */
	public <T extends Fluid> RegistryObject<T> fluid(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.FLUID, name, supplier);
	}

	/** Helper to register a still fluid, flowing fluid, and a bucket item in one call. */
	public <S extends Fluid, F extends Fluid> RegistryObject<S> fluid(
			String name,
			VisceralFluidProperties props,
			Supplier<S> stillSupplier,
			Supplier<F> flowingSupplier,
			Function<S, Item.Properties> bucketProperties
	) {
		//IFluidHelper.INSTANCE.registerFluidType(name, props.attributes(), registry);
		RegistryObject<S> still = fluid(name, stillSupplier);
		fluid("flowing_" + name, flowingSupplier);
		item(name + "_bucket",
				() -> new BucketItem(still.get(), bucketProperties
						.apply(still.get())
						.stacksTo(1)
						.craftRemainder(Items.BUCKET))
		);
		return still;
	}

	/** Shorthand for registering a Sound Event. */
	public <T extends SoundEvent> RegistryObject<T> sound(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.SOUND_EVENT, name, supplier);
	}

	/** Shorthand for registering a Menu Type. */
	public <T extends MenuType<?>> RegistryObject<T> menu(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.MENU, name, supplier);
	}

	/** Shorthand for registering a Particle Type. */
	public <T extends ParticleType<?>> RegistryObject<T> particle(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.PARTICLE_TYPE, name, supplier);
	}

	public <T extends CreativeModeTab> RegistryObject<T> tab(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.CREATIVE_MODE_TAB, name, supplier);
	}

	/** Shorthand for registering a Creative Mode Tab. */
	public RegistryObject<CreativeModeTab> autoTab(String name, Consumer<CreativeModeTab.Builder> builderConfig) {
		return tab(name, () -> ITabHelper.INSTANCE.create(builderConfig));
	}

	// --- Effects and Mechanics ---

	/** Shorthand for registering a Mob Effect. */
	public <T extends MobEffect> RegistryObject<T> effect(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.MOB_EFFECT, name, supplier);
	}

	/** Shorthand for registering an Entity Attribute. */
	public <T extends Attribute> RegistryObject<T> attribute(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ATTRIBUTE, name, supplier);
	}

	/** Shorthand for registering a Potion. */
	public <T extends Potion> RegistryObject<T> potion(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.POTION, name, supplier);
	}

	/** Shorthand for registering a Worldgen Feature. */
	public <T extends Feature<?>> RegistryObject<T> feature(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.FEATURE, name, supplier);
	}
}
