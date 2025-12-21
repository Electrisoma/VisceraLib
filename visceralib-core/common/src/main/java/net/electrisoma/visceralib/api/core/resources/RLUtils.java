package net.electrisoma.visceralib.api.core.resources;

import net.minecraft.resources.ResourceLocation;

public class RLUtils {

    /**
     * Creates a {@link ResourceLocation} using version-appropriate factory methods.
     * <p>
     * <b>Version Compatibility:</b>
     * <ul>
     * <li><b>1.21+:</b> Uses {@code ResourceLocation.fromNamespaceAndPath(namespace, path)}</li>
     * <li><b>1.20.x and below:</b> Uses {@code new ResourceLocation(namespace, path)}</li>
     * </ul>
     * @param namespace The namespace (usually your mod ID).
     * @param path      The path (the name of the texture, model, or data entry).
     * @return A new {@link ResourceLocation} instance.
     */
    public static ResourceLocation path(String namespace, String path) {
        return /*? =1.21.1 {*/ResourceLocation.fromNamespaceAndPath
                /*?} else {*//*new ResourceLocation*//*?}*/(namespace, path);
    }

    public static String getPathOrDefault(ResourceLocation location, String defaultValue) {
        return location == null ? defaultValue : location.getPath();
    }
}