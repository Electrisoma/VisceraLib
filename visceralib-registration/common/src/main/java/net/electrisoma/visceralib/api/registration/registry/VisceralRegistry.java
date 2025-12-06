package net.electrisoma.visceralib.api.registration.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.electrisoma.visceralib.api.registration.registry.builder.*;
import net.electrisoma.visceralib.api.registration.registry.holder.BaseHolder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Function;

public record VisceralRegistry(String modId) {

    private static final Multimap<Registry<?>, Registration<?, ?, ?>> REGISTRATIONS = HashMultimap.create();
    public static final Multimap<Registry<?>, Registration<?, ?, ?>> REGISTRATIONS_VIEW = Multimaps.unmodifiableMultimap(REGISTRATIONS);

    @Internal
    public static <R, T extends R, H extends BaseHolder<T>> void addToRegistrationMap(
            Registry<R> registry,
            Registration<R, T, H> registration
    ) {
        REGISTRATIONS.put(registry, registration);
    }

    public <B extends Block> BlockBuilder<B> block(String id, Function<BlockBehaviour.Properties, B> factory) {
        return new BlockBuilder<>(this, id, factory);
    }

    public <I extends Item> ItemBuilder<I> item(String id, Function<Item.Properties, I> factory) {
        return new ItemBuilder<>(this, id, factory);
    }

    public CreativeTabBuilder tab(String id) {
        return new CreativeTabBuilder(this, id);
    }

    public <BE extends BlockEntity> BlockEntityBuilder<BE> blockEntity(
            String id,
            BlockEntityBuilder.BlockEntityFactory<BE> factory
    ) {
        return new BlockEntityBuilder<>(this, id, factory);
    }

    public <E extends Entity> EntityBuilder<E> entity(
            String name,
            Registry<EntityType<?>> registry,
            EntityType.EntityFactory<E> factory,
            MobCategory category
    ) {
        return new EntityBuilder<>(this, name, registry, factory, category);
    }

//    public <DC> DataComponentTypeBuilder<DC> dataComponentType(
//            String id,
//            UnaryOperator<DataComponentType.Builder<DC>> builder
//    ) {
//        return new DataComponentTypeBuilder<>(this, id, builder);
//    }
}