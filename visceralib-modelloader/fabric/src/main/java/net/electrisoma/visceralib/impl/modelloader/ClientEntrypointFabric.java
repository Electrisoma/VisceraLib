package net.electrisoma.visceralib.impl.modelloader;

import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper.EnvironmentEnum;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public final class ClientEntrypointFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        IEnvHelper.INSTANCE.runIfCurrent(EnvironmentEnum.CLIENT, ClientEntrypoint::init);
        ModelLoadingPlugin.register(new FabricModelLoadingPlugin());
    }

    private static class FabricModelLoadingPlugin implements ModelLoadingPlugin {

        @Override
        public void onInitializeModelLoader(Context context) {

        }
    }
}