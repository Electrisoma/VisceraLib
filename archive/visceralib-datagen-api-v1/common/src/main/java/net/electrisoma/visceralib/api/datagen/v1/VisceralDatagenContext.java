package net.electrisoma.visceralib.api.datagen.v1;

public class VisceralDatagenContext {

	private static final String ACTIVE_MOD = System.getProperty("visceral.active_mod");

	public static final String PLATFORM = resolvePlatformName();
	public static final int FLUID_MULTIPLIER = "fabric".equals(PLATFORM) ? 81 : 1;

	private static String resolvePlatformName() {
		String modid = getModId();
		if (Boolean.getBoolean(modid + ".datagen.platform.fabric"))
			return "fabric";
		if (Boolean.getBoolean(modid + ".datagen.platform.neoforge"))
			return "neoforge";
		return "common";
	}

	public static boolean isPlatformActive() {
		return Boolean.getBoolean(getModId() + ".datagen.platform." + PLATFORM);
	}

	public static String getModId() {
		return ACTIVE_MOD == null ? "visceralib" : ACTIVE_MOD;
	}
}
