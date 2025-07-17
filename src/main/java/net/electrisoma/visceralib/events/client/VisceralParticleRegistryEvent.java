package net.electrisoma.visceralib.events.client;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class VisceralParticleRegistryEvent {
    @FunctionalInterface
    public interface ParticleFactoryRegistration {
        void register(ParticleFactoryRegistrar registrar);
    }

    public interface ParticleFactoryRegistrar {
        <T extends ParticleType<?>> void register(Supplier<T> type, Function<SpriteSet, ? extends ParticleProvider<?>> provider);
    }

    private static final List<ParticleFactoryRegistration> REGISTRATIONS = new ArrayList<>();

    public static void register(ParticleFactoryRegistration callback) {
        REGISTRATIONS.add(callback);
    }

    public static void fire(ParticleFactoryRegistrar registrar) {
        for (var registration : REGISTRATIONS)
            registration.register(registrar);
    }

    private VisceralParticleRegistryEvent() {}
}
