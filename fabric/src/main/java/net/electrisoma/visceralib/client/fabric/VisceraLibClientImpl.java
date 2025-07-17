package net.electrisoma.visceralib.client.fabric;

import net.electrisoma.visceralib.client.VisceralLibClient;
import net.electrisoma.visceralib.testreg.Particles;
import net.electrisoma.visceralib.testreg.particles.TestCloudParticle;
import net.electrisoma.visceralib.testreg.particles.TestParticleProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class VisceraLibClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VisceralLibClient.init();

        ParticleFactoryRegistry.getInstance().register(
                Particles.CLOUD.get(),
                TestParticleProvider::new
        );
    }
}
