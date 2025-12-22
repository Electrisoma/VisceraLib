package net.electrisoma.visceralib.api.registration.registry.builder;

import net.electrisoma.visceralib.api.registration.registry.Registration;
import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.BaseHolder;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class AbstractBuilder<R, T extends R, H extends BaseHolder<T>> {
    protected final VisceralRegistry owner;
    protected final ResourceLocation id;
    protected final Registry<R> registry;
    protected H holder;
    private Consumer<H> afterRegisterCallback = $ -> {};

    public AbstractBuilder(VisceralRegistry owner, String name, Registry<R> registry) {
        this.owner = owner;
        this.id = ResourceLocation.fromNamespaceAndPath(owner.modId, name);
        this.registry = registry;
    }

    public void chainAfterRegisterCallback(Consumer<H> callback) {
        afterRegisterCallback = afterRegisterCallback.andThen(callback);
    }

    abstract T build();

    abstract H getHolder(HolderOwner<R> owner, ResourceKey<R> key);

    public H register() {
        ResourceKey<R> key = ResourceKey.create(registry.key(), id);
        holder = getHolder(registry.holderOwner(), key);
        Registration<R, T, H> registration = new Registration<>(id, registry, this::build, holder, afterRegisterCallback);

        if (IPlatformHelper.INSTANCE.isCurrent(IPlatformHelper.PlatformEnum.FABRIC)) {
            registration.register();
        } else {
            VisceralRegistry.addToRegistrationMap(registry, registration);
        }

        return holder;
    }
}