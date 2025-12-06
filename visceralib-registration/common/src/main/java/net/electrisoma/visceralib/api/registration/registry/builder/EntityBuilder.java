package net.electrisoma.visceralib.api.registration.registry.builder;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.EntityHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Function;

public class EntityBuilder<E extends Entity> extends AbstractBuilder<EntityType<?>, EntityType<E>, EntityHolder<E>> {

    private final EntityType.Builder<E> builder;
    private Function<EntityType.Builder<E>, EntityType.Builder<E>> properties = Function.identity();

    public EntityBuilder(
            VisceralRegistry owner,
            String name,
            Registry<EntityType<?>> registry,
            EntityType.EntityFactory<E> factory,
            MobCategory category
    ) {
        super(owner, name, registry);
        this.builder = EntityType.Builder.of(factory, category);
    }

    public EntityBuilder<E> properties(Function<EntityType.Builder<E>, EntityType.Builder<E>> properties) {
        this.properties = this.properties.andThen(properties);
        return this;
    }

    @Override
    EntityType<E> build() {
        EntityType.Builder<E> builder = this.builder;
        builder = properties.apply(builder);
        return builder.build(id.toString());
    }

    @Override
    EntityHolder<E> getHolder(HolderOwner<EntityType<?>> owner, ResourceKey<EntityType<?>> key) {
        //noinspection unchecked,rawtypes
        return new EntityHolder<>((HolderOwner) owner, (ResourceKey) key);
    }
}