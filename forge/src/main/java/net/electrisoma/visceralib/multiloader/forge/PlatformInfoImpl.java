package net.electrisoma.visceralib.multiloader.forge;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.MavenVersionAdapter;

import java.util.List;

@SuppressWarnings("unused")
public class PlatformInfoImpl {
    public static PlatformInfo getCurrent() {
        return PlatformInfo.FORGE;
    }

    public static String findVersion() {
        String versionString = "UNKNOWN";

        List<IModInfo> infoList = ModList.get().getModFileById(VisceraLib.MOD_ID).getMods();
        if (infoList.size() > 1) VisceraLib.LOGGER.error("Multiple mods for MOD_ID: " + VisceraLib.MOD_ID);
        for (IModInfo info : infoList) {
            if (info.getModId().equals(VisceraLib.MOD_ID)) {
                versionString = String.valueOf(MavenVersionAdapter.createFromVersionSpec(String.valueOf(info.getVersion())));
                break;
            }
        }
        return versionString;
    }
}
