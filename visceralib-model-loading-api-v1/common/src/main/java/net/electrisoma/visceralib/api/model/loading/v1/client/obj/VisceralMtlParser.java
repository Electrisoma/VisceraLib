package net.electrisoma.visceralib.api.model.loading.v1.client.obj;

import net.electrisoma.visceralib.api.core.resources.RLUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VisceralMtlParser {

	private static final Map<ResourceLocation, Map<String, ResourceLocation>> MTL_CACHE = new ConcurrentHashMap<>();

	public static void clearCache() {
		MTL_CACHE.clear();
	}

	public static Map<String, ResourceLocation> parse(ResourceLocation loc) {
		return MTL_CACHE.computeIfAbsent(loc, VisceralMtlParser::loadMtl);
	}

	private static Map<String, ResourceLocation> loadMtl(ResourceLocation loc) {
		Map<String, ResourceLocation> materials = new HashMap<>();

		var resource = Minecraft.getInstance().getResourceManager().getResource(loc);
		if (resource.isEmpty()) return materials;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.get().open()))) {
			String currentMtl = null;
			String line;

			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;

				int spaceIdx = line.indexOf(' ');
				if (spaceIdx == -1) continue;

				String keyword = line.substring(0, spaceIdx).toLowerCase();
				String value = line.substring(spaceIdx + 1).trim();

				if (keyword.equals("newmtl")) {
					currentMtl = value;
				} else if (keyword.equals("map_kd") && currentMtl != null) {
					materials.put(currentMtl, sanitizeTexturePath(loc, value));
				}
			}
		} catch (Exception e) {
			System.err.println("VisceraLib: Failed to parse MTL file at " + loc + ": " + e.getMessage());
		}

		return materials;
	}

	private static ResourceLocation sanitizeTexturePath(ResourceLocation mtlLoc, String path) {
		if (path.startsWith("#")) {
			return RLUtils.path("visceral_variable", path.substring(1));
		}

		String cleanPath = path.toLowerCase().replace('\\', '/');

		if (cleanPath.endsWith(".png"))
			cleanPath = cleanPath.substring(0, cleanPath.length() - 4);

		try {
			if (cleanPath.contains(":")) {
				return RLUtils.parse(cleanPath);
			} else {
				return RLUtils.path(mtlLoc.getNamespace(), cleanPath);
			}
		} catch (Exception e) {
			return RLUtils.mc("missingno");
		}
	}
}
