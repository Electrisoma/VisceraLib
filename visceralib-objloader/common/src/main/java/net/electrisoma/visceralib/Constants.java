package net.electrisoma.visceralib;

import net.electrisoma.visceralib.platform.Services;
import net.electrisoma.visceralib.platform.services.IPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "visceralib";
    public static final String NAME = "VisceraLib";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final String LOADER = IPlatformHelper.INSTANCE.getPlatformName();
    public static final String VERSION = IPlatformHelper.INSTANCE.getVersion();

    public static ResourceLocation path(String id, String path) {
        return /*? =1.21.1 {*/ResourceLocation.fromNamespaceAndPath
                /*?} else {*//*new ResourceLocation*//*?}*/(id, path);
    }
}
