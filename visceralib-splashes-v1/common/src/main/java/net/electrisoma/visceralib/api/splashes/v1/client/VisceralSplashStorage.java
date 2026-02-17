package net.electrisoma.visceralib.api.splashes.v1.client;

import net.electrisoma.visceralib.event.splashes.v1.client.VisceralSplashEvent;
import net.electrisoma.visceralib.platform.splashes.v1.services.event.client.VisceraLibSplashesClientEvents;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Utility for discovering and loading custom splash texts from resource packs.
 * This allows mods to contribute to the main menu splash pool via {@code texts/splashes.txt}.
 */
public class VisceralSplashStorage {

	private static final Logger LOG = LoggerFactory.getLogger("VisceraLib/VisceralSplashStorage");
	private static final SplashStyle STYLE = new SplashStyle();
	private static String activePrioritySplash = null;

	public static SplashStyle getStyle() {
		return STYLE;
	}

	public static @Nullable String getPrioritySplash() {
		return activePrioritySplash;
	}

	public static List<String> loadAll(ResourceManager rm) {
		List<String> pool = new ArrayList<>();
		activePrioritySplash = null;
		STYLE.reset();

		VisceralSplashEvent.Context context = new SplashEventImpl(rm, STYLE, pool,
				s -> activePrioritySplash = s);

		VisceraLibSplashesClientEvents.INSTANCE.postSplashPre(context);

		rm.listResources("texts", id -> id.getPath().endsWith("splashes.txt"))
				.forEach((id, res) -> loadResource(id, res, pool));

		VisceraLibSplashesClientEvents.INSTANCE.postSplashPost(context);

		return pool;
	}

	public record SplashEventImpl(
			ResourceManager resourceManager,
			SplashStyle splashStyle,
			List<String> splashPool,
			Consumer<String> prioritySetter
	) implements VisceralSplashEvent.Context {

		@Override
		public ResourceManager getResourceManager() {
			return resourceManager;
		}

		@Override
		public SplashStyle getStyle() {
			return splashStyle;
		}

		@Override
		public List<String> getSplashPool() {
			return splashPool;
		}

		@Override
		public void setPrioritySplash(String s) {
			prioritySetter.accept(s);
		}

		@Override
		public void addSplash(String s) {
			splashPool.add(s);
		}

		@Override
		public void addSplashFile(ResourceLocation f) {
			resourceManager.getResource(f).ifPresent(r ->
					VisceralSplashStorage.loadResource(f, r, splashPool));
		}
	}

	public static void loadResource(ResourceLocation id, Resource res, List<String> pool) {
		try (BufferedReader reader = res.openAsReader()) {
			reader.lines()
					.map(String::trim)
					.filter(line -> !line.isEmpty() && !line.startsWith("#"))
					.forEach(pool::add);
		} catch (IOException e) {
			LOG.error("Failed to load splashes from {}: {}", id, e.getMessage());
		}
	}
}
