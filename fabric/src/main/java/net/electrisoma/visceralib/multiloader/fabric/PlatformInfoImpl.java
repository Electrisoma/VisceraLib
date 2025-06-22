package net.electrisoma.visceralib.multiloader.fabric;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformInfoImpl {
    public static PlatformInfo getCurrent() {
        return PlatformInfo.FABRIC;
    }

    public static String findVersion() {
        return FabricLoader.getInstance()
                .getModContainer(VisceraLib.MOD_ID)
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}
