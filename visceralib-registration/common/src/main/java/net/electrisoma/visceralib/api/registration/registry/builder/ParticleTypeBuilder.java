//package net.electrisoma.visceralib.api.registration.registry.builder;
//
//import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
//import net.electrisoma.visceralib.api.registration.registry.holder.ParticleHolder;
//import net.minecraft.core.HolderOwner;
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceKey;
//
//import java.util.function.Supplier;
//
//public class ParticleTypeBuilder<T extends ParticleType<?>> extends AbstractBuilder<ParticleType<?>, T, ParticleHolder<T>> {
//
//    private final Supplier<T> supplier;
//
//    public ParticleTypeBuilder(VisceralRegistry owner, String name, Supplier<T> supplier) {
//        super(owner, name, BuiltInRegistries.PARTICLE_TYPE);
//        this.supplier = supplier;
//    }
//
//    @Override
//    protected T build() {
//        return supplier.get();
//    }
//
//
//    @Override
//    @SuppressWarnings("unchecked")
//    ParticleHolder<T> getHolder(HolderOwner<ParticleType<?>> owner, ResourceKey<ParticleType<?>> key) {
//        return new ParticleHolder<>((HolderOwner<T>) owner, (ResourceKey<T>) key);
//    }
//}