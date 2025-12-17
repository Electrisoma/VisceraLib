package net.electrisoma.visceralib.platform.core;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

@AutoService(IPlatformHelper.class)
public final class PlatformHelperImpl implements IPlatformHelper {

    private String cachedModVersion;
    private String cachedMcVersion;

    @Override
    public PlatformEnum getPlatformInfo() {
        return PlatformEnum.FABRIC;
    }

    @Override
    public String getVersion(String id) {
        if (cachedModVersion == null) {
            cachedModVersion = FabricLoader.getInstance()
                    .getModContainer(id)
                    .map(container -> container
                            .getMetadata()
                            .getVersion()
                            .getFriendlyString()
                    )
                    .orElse("UNKNOWN");
        }
        return cachedModVersion;
    }

    @Override
    public String getMinecraftVersion() {
        if (cachedMcVersion == null) {
            cachedMcVersion = FabricLoader.getInstance()
                    .getModContainer("minecraft")
                    .map(container -> container
                            .getMetadata()
                            .getVersion()
                            .getFriendlyString()
                    )
                    .orElse("Unknown");
        }
        return cachedMcVersion;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
