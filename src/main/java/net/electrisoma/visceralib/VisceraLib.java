package net.electrisoma.visceralib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.electrisoma.visceralib.multiloader.VisceralRegistrySetup;
import net.electrisoma.visceralib.testreg.VisceralibModSetup;
import net.electrisoma.visceralib.testreg.VisceralibTabs;
import net.minecraft.resources.ResourceLocation;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class VisceraLib {
	public static final String MOD_ID = "visceralib";
	public static final String NAME = "VisceraLib";
	public static final String VERSION = PlatformInfo.findVersion();
	public static final PlatformInfo LOADER = PlatformInfo.getCurrent();
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final VisceralRegistrar REGISTRAR = VisceralRegistrar
			.create(MOD_ID)
			.defaultCreativeTab(() -> VisceralibTabs.BASE);

	public static void init() {
		VisceralRegistrySetup.init();
	}

	public static VisceralRegistrar registrar() {
		return REGISTRAR;
	}

	public static ResourceLocation path(String id, String path) {
		return /*? =1.21.1 {*//*ResourceLocation.fromNamespaceAndPath
				*//*?} else {*/new ResourceLocation/*?}*/(id, path);
	}
}