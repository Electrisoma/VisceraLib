package net.electrisoma.resotech.multiloader.neoforge;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.multiloader.PlatformInfo;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.MavenVersionAdapter;

import java.util.List;

@SuppressWarnings("unused")
public class PlatformInfoImpl {
    public static PlatformInfo getCurrent() {
        return PlatformInfo.NEOFORGE;
    }

    public static String findVersion() {
        String versionString = "UNKNOWN";

        List<IModInfo> infoList = ModList.get().getModFileById(ResoTech.MOD_ID).getMods();
        if (infoList.size() > 1) ResoTech.LOGGER.error("Multiple mods for MOD_ID: " + ResoTech.MOD_ID);
        for (IModInfo info : infoList) {
            if (info.getModId().equals(ResoTech.MOD_ID)) {
                versionString = String.valueOf(MavenVersionAdapter.createFromVersionSpec(String.valueOf(info.getVersion())));
                break;
            }
        }
        return versionString;
    }
}
