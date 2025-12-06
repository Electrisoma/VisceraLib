package net.electrisoma.visceralib.api.core;

import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.minecraft.resources.ResourceLocation;

public class PlatformConstants {

    public static String getLoader() {
        return IPlatformHelper.INSTANCE.getPlatformName();
    }
    public static String getVersion() {
        return IPlatformHelper.INSTANCE.getVersion();
    }

    /**
     * Game version wrapper for {@link ResourceLocation} using Stonecutter
     * @param namespace The {@link ResourceLocation#namespace}
     * @param path The {@link ResourceLocation#path}
     * @return The {@link ResourceLocation}
     */
    public static ResourceLocation path(String namespace, String path) {
        return /*? =1.21.1 {*/ResourceLocation.fromNamespaceAndPath
                /*?} else {*//*new ResourceLocation*//*?}*/(namespace, path);
    }
}
