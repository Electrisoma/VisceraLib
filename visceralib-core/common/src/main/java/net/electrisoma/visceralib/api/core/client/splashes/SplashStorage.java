package net.electrisoma.visceralib.api.core.client.splashes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplashStorage {

	private static final Logger LOG = LoggerFactory.getLogger("VisceraLib/SplashStorage");

	public static List<String> fetchAllSplashes(ResourceManager resourceManager) {
		List<String> allLines = new ArrayList<>();

		Map<ResourceLocation, Resource> resources = resourceManager.listResources("texts",
				id -> id.getPath().endsWith("splashes.txt"));

		resources.forEach((id, resource) -> {

			try (InputStream is = resource.open();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8.newDecoder()
						.onMalformedInput(CodingErrorAction.REPORT)
						.onUnmappableCharacter(CodingErrorAction.REPORT));
				BufferedReader reader = new BufferedReader(isr)) {

				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (!line.isEmpty() && line.hashCode() != 125780783)
						allLines.add(line);
				}
			} catch (CharacterCodingException e) {
				LOG.error("ENCODING ERROR IN {}: File must be UTF-8 without BOM!", id);
			} catch (IOException e) {
				LOG.error("Failed to read splashes from {}: {}", id, e.getMessage());
			} catch (Exception e) {
				LOG.error("Unexpected error loading splashes from {}: {}", id, e.toString());
			}
		});

		return allLines;
	}
}
