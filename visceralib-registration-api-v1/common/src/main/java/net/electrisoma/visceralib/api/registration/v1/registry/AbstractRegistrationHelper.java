package net.electrisoma.visceralib.api.registration.v1.registry;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluidProperties;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractRegistrationHelper<SELF extends AbstractRegistrationHelper<SELF>> {

    protected final VisceralRegistry registry;

    protected AbstractRegistrationHelper(VisceralRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    protected final SELF self() {
        return (SELF) this;
    }

    public <R, T extends R> RegistryObject<T> register(
            Registry<R> registry,
            String name,
            Supplier<T> supplier
    ) {
        return this.registry.register(registry, name, supplier);
    }

    //  blocks and items

    public <T extends Block> RegistryObject<T> block(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.BLOCK, name, supplier);
    }

    // 1.21.11 backport
    public <T extends Block> RegistryObject<T> block(
            String name,
            Function<BlockBehaviour.Properties, ? extends T> factory,
            Supplier<BlockBehaviour.Properties> supplier
    ) {
        return register(BuiltInRegistries.BLOCK, name, () -> factory.apply(supplier.get()));
    }

    public <T extends Item> RegistryObject<T> item(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.ITEM, name, supplier);
    }

    // 1.21.11 backport
    public <T extends Item> RegistryObject<T> item(
            String name,
            Function<Item.Properties, ? extends T> factory,
            Supplier<Item.Properties> supplier
    ) {
        return register(BuiltInRegistries.ITEM, name, () -> factory.apply(supplier.get()));
    }

    public <T extends Block> RegistryObject<T> blockWithItem(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> blockObj = block(name, blockSupplier);
        item(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }

    // 1.21.11 backport
    public <T extends Block> RegistryObject<T> blockWithItem(
            String name,
            Function<BlockBehaviour.Properties, ? extends T> factory,
            Supplier<BlockBehaviour.Properties> supplier
    ) {
        RegistryObject<T> blockObj = block(name, () -> factory.apply(supplier.get()));
        item(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }

    // gameplay

    public <T extends BlockEntityType<?>> RegistryObject<T> blockEntityType(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, supplier);
    }

    // 1.21.11 backport
    public <T extends BlockEntityType<?>> RegistryObject<T> blockEntityType(
            String name,
            Function<BlockEntityType.BlockEntitySupplier<?>, ? extends T> factory,
            Supplier<BlockEntityType.BlockEntitySupplier<?>> supplier
    ) {
        return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, () -> factory.apply(supplier.get()));
    }

    public <T extends EntityType<?>> RegistryObject<T> entityType(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.ENTITY_TYPE, name, supplier);
    }

    // 1.21.11 backport
    public <T extends EntityType<?>> RegistryObject<T> entityType(
            String name,
            Function<EntityType.EntityFactory<?>, ? extends T> factory,
            Supplier<EntityType.EntityFactory<?>> supplier
    ) {
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

    public <T extends ParticleType<?>> RegistryObject<T> particle(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.PARTICLE_TYPE, name, supplier);
    }

    public <T extends CreativeModeTab> RegistryObject<T> tab(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.CREATIVE_MODE_TAB, name, supplier);
    }

    public RegistryObject<CreativeModeTab> autoTab(String name, Consumer<CreativeModeTab.Builder> builderConfig) {
        return tab(name, () -> ITabHelper.INSTANCE.create(builderConfig));
    }

    // effects and mechanics

    public <T extends MobEffect> RegistryObject<T> effect(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.MOB_EFFECT, name, supplier);
    }

    public <T extends Attribute> RegistryObject<T> attribute(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.ATTRIBUTE, name, supplier);
    }

    public <T extends Potion> RegistryObject<T> potion(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.POTION, name, supplier);
    }

    public <T extends Feature<?>> RegistryObject<T> feature(String name, Supplier<T> supplier) {
        return register(BuiltInRegistries.FEATURE, name, supplier);
    }
}