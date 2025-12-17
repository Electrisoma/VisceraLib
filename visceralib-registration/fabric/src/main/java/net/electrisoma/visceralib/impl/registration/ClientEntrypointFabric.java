package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.fabricmc.api.ClientModInitializer;

public class ClientEntrypointFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
    }
}
