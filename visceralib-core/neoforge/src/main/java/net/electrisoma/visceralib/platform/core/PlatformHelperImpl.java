package net.electrisoma.visceralib.platform.core;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

@AutoService(IPlatformHelper.class)
public final class PlatformHelperImpl implements IPlatformHelper {

    private String cachedModVersion;
    private String cachedMcVersion;

    @Override
    public PlatformEnum getPlatformInfo() {
        return PlatformEnum.NEOFORGE;
    }

    @Override
    public String getVersion(String id) {
        if (cachedModVersion == null) {
            cachedModVersion = ModList.get()
                    .getModFileById(id).getMods().stream()
                    .filter(info -> info.getModId().equals(id))
                    .findFirst()
                    .map(info -> info.getVersion().toString())
                    .orElse("UNKNOWN");
        }
        return cachedModVersion;
    }

    @Override
    public String getMinecraftVersion() {
        if (cachedMcVersion == null) {
            cachedMcVersion = ModList.get()
                    .getModContainerById("minecraft")
                    .map(container -> container
                            .getModInfo()
                            .getVersion()
                            .toString()
                    )
                    .orElse("UNKNOWN");
        }
        return cachedMcVersion;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDev() {
        return !FMLLoader.isProduction();
    }
}
