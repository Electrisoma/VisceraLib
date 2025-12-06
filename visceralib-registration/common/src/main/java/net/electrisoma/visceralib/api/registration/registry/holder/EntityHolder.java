package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EntityHolder<T extends Entity> extends BaseHolder<EntityType<T>> {

    public EntityHolder(HolderOwner<EntityType<T>> owner, ResourceKey<EntityType<T>> key) {
        super(owner, key);
    }

    public @Nullable T createEntity(Level level) {
        return value().create(level);
    }

    public boolean is(@Nullable Entity entity) {
        return entity != null && entity.getType() == value();
    }
}