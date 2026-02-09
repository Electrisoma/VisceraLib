package net.electrisoma.visceralib.api.core.resources;

import net.minecraft.resources.ResourceLocation;

public class RLUtils {

    /**
     * Simple ResourceLocation.fromNamespaceAndPath wrapper to make typing out a resource location more bearable.
     * Can also be expanded in the future to have versioned replacements with support for newer versions.
     *
     * @param namespace the namespace (usually your mod ID).
     * @param path      the path (the name of the texture, model, or data entry).
     * @return A new {@link ResourceLocation} instance.
     */
    public static ResourceLocation path(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    /**
     * Simple ResourceLocation.withDefaultNamespace wrapper to make typing the minecraft resource location easier.
     *
     * @param path the path (the name of the texture, model, or data entry).
     * @return A new {@link ResourceLocation} instance.
     */
    public static ResourceLocation mc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static String getPathOrDefault(ResourceLocation location, String defaultValue) {
        return location == null ? defaultValue : location.getPath();
    }
}