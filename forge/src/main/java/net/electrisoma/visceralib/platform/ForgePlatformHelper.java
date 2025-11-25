package net.electrisoma.visceralib.platform;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.Constants;
import net.electrisoma.visceralib.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.MavenVersionAdapter;

import java.util.List;

@AutoService(IPlatformHelper.class)
public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public PlatformEnum getPlatformInfo() {
        return PlatformEnum.FORGE;
    }

    @Override
    public String getVersion() {
        String versionString = "UNKNOWN";

        List<IModInfo> infoList = ModList.get().getModFileById(Constants.MOD_ID).getMods();
        if (infoList.size() > 1) Constants.LOGGER.error("Multiple mods for MOD_ID: " + Constants.MOD_ID);
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
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
