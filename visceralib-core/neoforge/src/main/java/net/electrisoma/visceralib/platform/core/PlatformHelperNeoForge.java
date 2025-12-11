package net.electrisoma.visceralib.platform.core;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.impl.core.Constants;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.MavenVersionAdapter;

import java.util.List;

@AutoService(IPlatformHelper.class)
public class PlatformHelperNeoForge implements IPlatformHelper {

    @Override
    public PlatformEnum getPlatformInfo() {
        return PlatformEnum.NEOFORGE;
    }

    @Override
    public String getVersion() {
        String versionString = "UNKNOWN";

        List<IModInfo> infoList = ModList.get().getModFileById(Constants.MOD_ID).getMods();
        if (infoList.size() > 1) Constants.LOG.error("Multiple mods for MOD_ID: " + Constants.MOD_ID);
        for (IModInfo info : infoList) {
            if (info.getModId().equals(Constants.MOD_ID)) {
                versionString = String.valueOf(MavenVersionAdapter.createFromVersionSpec(String.valueOf(info.getVersion())));
                break;
            }
        }
        return versionString;
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
