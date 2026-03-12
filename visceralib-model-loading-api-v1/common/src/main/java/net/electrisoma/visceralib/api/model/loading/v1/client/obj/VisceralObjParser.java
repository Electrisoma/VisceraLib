package net.electrisoma.visceralib.api.model.loading.v1.client.obj;

import net.electrisoma.visceralib.api.core.resources.RLUtils;

import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class VisceralObjParser {

	public static VisceralObjGeometry parse(ResourceLocation objLoc, InputStream stream, boolean flipV, int tintIndex) throws Exception {
		FloatArrayList positions = new FloatArrayList();
		FloatArrayList textures = new FloatArrayList();
		FloatArrayList normals = new FloatArrayList();

		Map<String, IntArrayList> materialGroups = new HashMap<>();
		Map<String, ResourceLocation> materialMap = new HashMap<>();

		String currentMaterial = "default";
		materialGroups.put(currentMaterial, new IntArrayList());

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;

				int firstSpace = line.indexOf(' ');
				if (firstSpace == -1) continue;

				String cmd = line.substring(0, firstSpace);
				String args = line.substring(firstSpace + 1).trim();

				switch (cmd) {
					case "usemtl" -> {
						currentMaterial = args;
						materialGroups.computeIfAbsent(currentMaterial, k -> new IntArrayList());
					}
					case "mtllib" -> {
						materialMap.putAll(VisceralMtlParser.parse(resolveRelative(objLoc, args)));
					}
					case "v" -> { // vertex
						parseFloats(args, positions);
					}
					case "vt" -> { // vertex texture coordinates
						parseFloats(args, textures);
					}
					case "vn" -> { // vertex normals
						parseFloats(args, normals);
					}
					case "f" -> { // face
						parseFace(args, materialGroups.get(currentMaterial));
					}
				}
			}
		}

		float[] texArray = textures.toFloatArray();
		if (flipV) {
			for (int i = 1; i < texArray.length; i += 2)
				texArray[i] = 1.0f - texArray[i];
		}

		return new VisceralObjGeometry(
				positions.toFloatArray(),
				texArray,
				normals.toFloatArray(),
				materialGroups,
				materialMap,
				tintIndex
		);
	}

	private static void parseFloats(String data, FloatArrayList list) {
		String[] parts = data.split("\\s+");
		Arrays.stream(parts).map(Float::parseFloat).forEach(list::add);
	}

	private static void parseFace(String data, IntArrayList output) {
		String[] vertices = data.split("\\s+");

		int vertexCount = Math.min(vertices.length, 4);

		for (int i = 0; i < vertexCount; i++) {
			String[] parts = vertices[i].split("/", -1);
			for (int j = 0; j < 3; j++) {
				if (j < parts.length && !parts[j].isEmpty()) {
					output.add(Integer.parseInt(parts[j]) - 1);
				} else {
					output.add(-1);
				}
			}
		}

		if (vertexCount == 3) {
			int last = output.size() - 3;
			output.add(output.getInt(last));
			output.add(output.getInt(last + 1));
			output.add(output.getInt(last + 2));
		}
	}

	private static ResourceLocation resolveRelative(ResourceLocation base, String path) {
		String basePath = base.getPath();
		int lastSlash = basePath.lastIndexOf('/');
		String newPath = (lastSlash >= 0 ? basePath.substring(0, lastSlash + 1) : "") + path;
		return RLUtils.path(base.getNamespace(), newPath);
	}
}
