package net.electrisoma.visceralib.impl.core;

import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.fabricmc.api.ClientModInitializer;

public class ClientEntrypointFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        IEnvHelper.INSTANCE.runIfCurrent(IEnvHelper.EnvironmentEnum.CLIENT, ClientEntrypoint::init);
    }
}
