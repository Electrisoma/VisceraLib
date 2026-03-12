package net.electrisoma.visceralib.api.registration.v1.registry.helper;

import net.electrisoma.visceralib.api.registration.v1.registry.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.v1.registry.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.api.registration.v1.registry.dynamic.DynamicRegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.fluid.VisceralFluidProperties;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

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
	 * Internal method to onRegister an entry to a specified registry.
	 *
	 * @param registry the target vanilla registry.
	 * @param name     the registry name of the object.
	 * @param supplier a supplier returning the object to onRegister.
	 * @return a RegistryObject wrapping the registered entry.
	 */
	public <R, T extends R> RegistryObject<T> register(
			Registry<R> registry,
			String name,
			Supplier<T> supplier
	) {
		return this.registry.register(registry, name, supplier);
	}

	/**
	 * Creates a helper focused on a specific registry.
	 * @param target the registry to bind to.
	 */
	public <T> BoundRegistryHelper<T> forRegistry(Registry<T> target) {
		return new BoundRegistryHelper<>(this.registry, target);
	}

	/** Registers a new custom static registry with default settings. */
	public <T> void newStaticRegistry(ResourceKey<Registry<T>> key, Consumer<Registry<T>> onCreated) {
		this.newStaticRegistry(key, VisceralRegistrySettings.DEFAULT, onCreated);
	}

	/** Registers a new custom static registry with specific settings. */
	public <T> void newStaticRegistry(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings, Consumer<Registry<T>> onCreated) {
		this.registry.newStaticRegistry(key, settings, onCreated);
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

	public <T extends Block> RegistryObject<T> block(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.BLOCK, name, supplier);
	}

	public <T extends Block> RegistryObject<T> block(String name, Function<BlockBehaviour.Properties, ? extends T> factory, Supplier<BlockBehaviour.Properties> props) {
		return register(BuiltInRegistries.BLOCK, name, () -> factory.apply(props.get()));
	}

	public <T extends Item> RegistryObject<T> item(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ITEM, name, supplier);
	}

	public <T extends Item> RegistryObject<T> item(String name, Function<Item.Properties, ? extends T> factory, Supplier<Item.Properties> props) {
		return register(BuiltInRegistries.ITEM, name, () -> factory.apply(props.get()));
	}

	public <T extends Block> RegistryObject<T> blockWithItem(String name, Supplier<T> blockSupplier) {
		RegistryObject<T> blockObj = block(name, blockSupplier);
		this.registry.addPostRegisterCallback(BuiltInRegistries.BLOCK.key(), blockObj.key().location(), () ->
				item(name, () -> new BlockItem(blockObj.get(), new Item.Properties()))
		);
		return blockObj;
	}

	public <T extends Block> RegistryObject<T> blockWithItem(String name, Function<BlockBehaviour.Properties, ? extends T> factory, Supplier<BlockBehaviour.Properties> props) {
		RegistryObject<T> blockObj = block(name, () -> factory.apply(props.get()));
		this.registry.addPostRegisterCallback(BuiltInRegistries.BLOCK.key(), blockObj.key().location(), () ->
				item(name, p -> new BlockItem(blockObj.get(), p), Item.Properties::new)
		);
		return blockObj;
	}

	public <T extends BlockEntityType<?>> RegistryObject<T> blockEntityType(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, supplier);
	}

	public <T extends BlockEntityType<?>> RegistryObject<T> blockEntityType(String name, Function<BlockEntityType.BlockEntitySupplier<?>, ? extends T> factory, Supplier<BlockEntityType.BlockEntitySupplier<?>> supplier) {
		return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, () -> factory.apply(supplier.get()));
	}

	public <T extends EntityType<?>> RegistryObject<T> entityType(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ENTITY_TYPE, name, supplier);
	}

	public <T extends EntityType<?>> RegistryObject<T> entityType(String name, Function<EntityType.EntityFactory<?>, ? extends T> factory, Supplier<EntityType.EntityFactory<?>> supplier) {
		return register(BuiltInRegistries.ENTITY_TYPE, name, () -> factory.apply(supplier.get()));
	}

	public <T extends Fluid> RegistryObject<T> fluid(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.FLUID, name, supplier);
	}

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

	public <T extends SoundEvent> RegistryObject<T> sound(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.SOUND_EVENT, name, supplier);
	}

	public <T extends MenuType<?>> RegistryObject<T> menu(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.MENU, name, supplier);
	}

	public <T extends MenuType<?>> RegistryObject<T> menu(String name, Function<MenuType.MenuSupplier<?>, ? extends T> factory, Supplier<MenuType.MenuSupplier<?>> supplier) {
		return register(BuiltInRegistries.MENU, name, () -> factory.apply(supplier.get()));
	}

	public <T extends ParticleType<?>> RegistryObject<T> particle(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.PARTICLE_TYPE, name, supplier);
	}

	public <T extends CreativeModeTab> RegistryObject<T> tab(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.CREATIVE_MODE_TAB, name, supplier);
	}

	public RegistryObject<CreativeModeTab> autoTab(String name, Consumer<CreativeModeTab.Builder> builderConfig) {
		return tab(name, () -> ITabHelper.INSTANCE.create(builderConfig));
	}

	public <T extends DataComponentType<?>> RegistryObject<T> dataComponent(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.DATA_COMPONENT_TYPE, name, supplier);
	}

	public <T extends RecipeType<?>> RegistryObject<T> recipeType(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.RECIPE_TYPE, name, supplier);
	}

	public <T extends RecipeSerializer<?>> RegistryObject<T> recipeSerializer(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.RECIPE_SERIALIZER, name, supplier);
	}

	public <T extends CriterionTrigger<?>> RegistryObject<T> trigger(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.TRIGGER_TYPES, name, supplier);
	}

	public <T extends LootItemConditionType> RegistryObject<T> lootCondition(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.LOOT_CONDITION_TYPE, name, supplier);
	}

	public <T extends LootItemFunctionType<?>> RegistryObject<T> lootFunction(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.LOOT_FUNCTION_TYPE, name, supplier);
	}

	public <T extends MobEffect> RegistryObject<T> effect(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.MOB_EFFECT, name, supplier);
	}

	public <T extends Attribute> RegistryObject<T> attribute(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ATTRIBUTE, name, supplier);
	}

	public <T extends ArmorMaterial> RegistryObject<T> armorMaterial(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.ARMOR_MATERIAL, name, supplier);
	}

	public <T extends Potion> RegistryObject<T> potion(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.POTION, name, supplier);
	}

	public <T extends Feature<?>> RegistryObject<T> feature(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.FEATURE, name, supplier);
	}

	public <T extends PoiType> RegistryObject<T> poiType(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, name, supplier);
	}

	public <T extends VillagerProfession> RegistryObject<T> villagerProfession(String name, Supplier<T> supplier) {
		return register(BuiltInRegistries.VILLAGER_PROFESSION, name, supplier);
	}
}
