package net.electrisoma.visceralib;

import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.electrisoma.visceralib.multiloader.VisceralRegistrySetup;
import net.minecraft.resources.ResourceLocation;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class VisceraLib {
	public static final String MOD_ID = "visceralib";
	public static final String NAME = "VisceraLib";
	public static final String VERSION = PlatformInfo.findVersion();
	public static final PlatformInfo LOADER = PlatformInfo.getCurrent();
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		LOGGER.info("{} {} initializing! on platform: {}", NAME, VERSION, LOADER);

		VisceralRegistrySetup.init();
	}

	public static ResourceLocation path(String id, String path) {
		//? if = 1.21.1
		return ResourceLocation.fromNamespaceAndPath(id, path);
		//? if < 1.21.1
		/*return new ResourceLocation(id, path);*/
	}
}