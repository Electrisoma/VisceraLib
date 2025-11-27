package net.electrisoma.visceralib.platform;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.Constants;
import net.electrisoma.visceralib.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

@AutoService(IPlatformHelper.class)
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public PlatformEnum getPlatformInfo() {
        return PlatformEnum.FABRIC;
    }

    @Override
    public String getVersion() {
        return FabricLoader.getInstance()
                .getModContainer(Constants.MOD_ID)
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
