package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.ParticleEntry;
import net.electrisoma.visceralib.testreg.particles.TestParticleProvider;

import net.minecraft.core.particles.SimpleParticleType;

public class Particles {
    public static void init() {
        VisceraLib.LOGGER.info("Registering Particles for " + VisceraLib.NAME);
    }

    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static final ParticleEntry<SimpleParticleType> CLOUD = REGISTRAR
            .particle("cloud_test", () -> new SimpleParticleType(false))
            .factory(TestParticleProvider::new)
            .register();
}
