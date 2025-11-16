package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.ParticleEntry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.function.Supplier;

public class ParticleBuilder<T extends ParticleType<?>, R extends AbstractVisceralRegistrar<R>>
        extends AbstractBuilder<T, R, ParticleBuilder<T, R>> {

    private final VisceralDeferredRegister<ParticleType<?>> register;
    private final Supplier<T> supplier;
    private VisceralRegistrySupplier<T> registeredSupplier;

    public ParticleBuilder(R registrar, String name, Supplier<T> supplier) {
        super(registrar, name);
        this.supplier = supplier;
        this.register = registrar.deferredRegister(Registries.PARTICLE_TYPE);
    }

    @SuppressWarnings("unchecked")
    public ParticleEntry<T> register() {
        VisceralRegistrySupplier<ParticleType<?>> raw =
                register.register(name, (Supplier<ParticleType<?>>) supplier);

        VisceralRegistrySupplier<T> typed = new VisceralRegistrySupplier<>(
                (ResourceKey<T>) raw.getKey(),
                () -> (T) raw.get()
        );

        this.registeredSupplier = typed;

        return new ParticleEntry<>(typed);
    }

    @Override
    public Optional<VisceralRegistrySupplier<T>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }

    @Override
    protected ParticleBuilder<T, R> self() {
        return this;
    }
}
