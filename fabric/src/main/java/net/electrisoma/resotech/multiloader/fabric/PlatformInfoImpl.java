package net.electrisoma.resotech.multiloader.fabric;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.multiloader.PlatformInfo;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformInfoImpl {
    public static PlatformInfo getCurrent() {
        return PlatformInfo.FABRIC;
    }

    public static String findVersion() {
        return FabricLoader.getInstance()
                .getModContainer(ResoTech.MOD_ID)
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}
