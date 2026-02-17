package net.electrisoma.visceralib.api.core.resources;

import net.minecraft.resources.ResourceLocation;

/**
 * Shorthand utilities for working with {@link ResourceLocation} instances.
 * Provides cleaner syntax for creating mod-specific and vanilla Minecraft identifiers.
 */
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

	/**
	 * Safely retrieves the path from a {@link ResourceLocation}.
	 *
	 * @param location     the resource location to query (can be null).
	 * @param defaultValue the value to return if the location is null.
	 * @return The path string of the location, or the default value.
	 */
	public static String getPathOrDefault(ResourceLocation location, String defaultValue) {
		return location == null ? defaultValue : location.getPath();
	}
}
