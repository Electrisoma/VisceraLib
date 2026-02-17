package net.electrisoma.visceralib.event.registration.v1.client;

@FunctionalInterface
public interface ParticleRegistrationNamespaced {

    void register(ParticleRegistrar registrar);
}