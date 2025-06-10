package net.electrisoma.resotech;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.lang.LangBuilder;

import net.electrisoma.resotech.multiloader.PlatformInfo;
import net.electrisoma.resotech.registry.ModSetup;
import net.minecraft.resources.ResourceLocation;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

@SuppressWarnings("unused")
public class ResoTech {
	public static final String MOD_ID = "resotech";
	public static final String NAME = "ResoTech";
	public static final String SERVER_START = "waaa";
	public static final String VERSION = PlatformInfo.findVersion();
	public static final PlatformInfo LOADER = PlatformInfo.getCurrent();
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		LOGGER.info("{} {} initializing! on platform: {}", NAME, VERSION, LOADER);

		ModSetup.register();

	}

	@SuppressWarnings("UnimplementedExpectPlatform")
    @ExpectPlatform
	public static void onRegister() {
		throw new AssertionError();
	}

	public static LangBuilder lang() {
		return new LangBuilder(MOD_ID);
	}

	public static ResourceLocation path(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}