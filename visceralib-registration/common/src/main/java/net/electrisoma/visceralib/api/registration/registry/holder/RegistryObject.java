package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.resources.ResourceKey;

/**
 * General wrapper for registration holders
 * use it for registering content with the builders as a simple alternative to the respective holders
 * @param holder
 */
public record RegistryObject<T>(BaseHolder<T> holder) {

    public T get() {
        return holder.value();
    }

    public <H extends BaseHolder<T>> H getHolder() {
        //noinspection unchecked
        return (H) this.holder;
    }

    public ResourceKey<T> getResourceKey() {
        return holder.key();
    }

    public static <T> RegistryObject<T> of(BaseHolder<T> holder) {
        return new RegistryObject<>(holder);
    }
}