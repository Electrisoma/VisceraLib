package net.electrisoma.visceralib;

import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.electrisoma.visceralib.multiloader.VisceralRegistrySetup;
import net.electrisoma.visceralib.registry.ModSetup;
import net.electrisoma.visceralib.registry.VisceraLibTabs;
import net.minecraft.resources.ResourceLocation;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class VisceraLib {
	public static final String MOD_ID = "visceralib";
	public static final String NAME = "VisceraLib";
	public static final String VERSION = PlatformInfo.findVersion();
	public static final PlatformInfo LOADER = PlatformInfo.getCurrent();
	public static final Logger LOGGER = LogUtils.getLogger();

	private static final VisceralRegistrar REGISTRAR = VisceralRegistrar.create(MOD_ID)
			.defaultCreativeTab(() -> VisceraLibTabs.BASE);

	public static void init() {
		LOGGER.info("{} {} initializing! on platform: {}", NAME, VERSION, LOADER);

		VisceralRegistrySetup.init();

		ModSetup.register();
	}

	public static ResourceLocation path(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static VisceralRegistrar registrar() {
		return REGISTRAR;
	}
}