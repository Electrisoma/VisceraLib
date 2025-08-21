package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.Registration;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractBuilder<R, T extends R, H extends Holder.Reference<T>> {
    private final VisceralRegistry owner;
    private final ResourceLocation id;
    private final Registry<R> registry;

    public AbstractBuilder(VisceralRegistry owner, String name, Registry<R> registry) {
        this.owner = owner;
        this.id = ResourceLocation.fromNamespaceAndPath(owner.modId, name);
        this.registry = registry;
    }

    abstract T build();

    abstract H getHolder(HolderOwner<R> owner, ResourceKey<R> key);

    public H register() {
        ResourceKey<R> key = ResourceKey.create(registry.key(), id);
        H holder = getHolder(registry.holderOwner(), key);
        Registration<R, T> registration = new Registration<>(id, registry, this::build, holder);

        if (PlatformInfo.FABRIC.isCurrent()) {
            registration.register();
        } else {
            VisceralRegistry.addToRegistrationMap(registry, registration);
        }

        return holder;
    }
}