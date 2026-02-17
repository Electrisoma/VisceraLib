package net.electrisoma.visceralib.event.splashes.v1.client;

import net.electrisoma.visceralib.api.splashes.v1.client.SplashStyle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Calendar;
import java.util.List;

public final class VisceralSplashEvent {

	private VisceralSplashEvent() {}

	public interface Context {

		ResourceManager getResourceManager();
		SplashStyle getStyle();
		List<String> getSplashPool();

		void setPrioritySplash(String splash);
		void addSplash(String splash);
		void addSplashFile(ResourceLocation file);

		default void addSeasonal(int month, int day, ResourceLocation file) {
			Calendar calendar = Calendar.getInstance();
			if (calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day)
				addSplashFile(file);
		}
	}

	@FunctionalInterface
	public interface Pre {

		void onSplashPre(Context context);
	}

	@FunctionalInterface
	public interface Post {

		void onSplashPost(Context context);
	}
}
