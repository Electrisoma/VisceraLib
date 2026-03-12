package net.electrisoma.visceralib.api.model.loading.v1.client.obj;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.VisceralUnbakedGeometry;
import net.electrisoma.visceralib.api.model.loading.v1.client.model.geometry.VisceralGeometryLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VisceralObjLoader implements VisceralGeometryLoader {

	private static final Map<ResourceLocation, VisceralObjGeometry> CACHE = new ConcurrentHashMap<>();

	public static void clearCache() {
		CACHE.clear();
		VisceralMtlParser.clearCache();
	}

	@Override
	public VisceralUnbakedGeometry read(JsonObject json) {
		if (!json.has("model"))
			throw new RuntimeException("OBJ Model JSON is missing the 'model' field pointing to the .obj file!");

		ResourceLocation loc = RLUtils.parse(json.get("model").getAsString());
		ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

		boolean flipV = json.has("flip_v") && json.get("flip_v").getAsBoolean();
		int tintIndex = json.has("tint_index") ? json.get("tint_index").getAsInt() : -1;

		return CACHE.computeIfAbsent(loc, l -> resourceManager.getResource(l).map(
				res -> {
					try (var stream = res.open()) {
						return VisceralObjParser.parse(l, stream, flipV, tintIndex);
					} catch (Exception e) {
						throw new RuntimeException("Visceralib failed to parse OBJ: " + l, e);
					}
				}).orElseThrow(() -> new RuntimeException("Visceralib could not find OBJ file at: " + l))
		);
	}
}
