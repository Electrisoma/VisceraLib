package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.platform.core.services.IResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "visceralib_registration";
    public static final String NAME = "VisceraLib-Registration";
    public static final Logger LOG = LoggerFactory.getLogger(NAME);

    public static ResourceLocation path(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}