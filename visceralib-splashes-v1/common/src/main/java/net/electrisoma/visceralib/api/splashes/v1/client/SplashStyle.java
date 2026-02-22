package net.electrisoma.visceralib.api.splashes.v1.client;

import net.minecraft.Util;
import net.minecraft.util.Mth;

public class SplashStyle {

	public int color = 0xFFFFFF00;
	public float scale = 1.0F;
	public boolean rainbow = false;

	public void reset() {
		this.color = 0xFFFFFF00;
		this.scale = 1.0F;
		this.rainbow = false;
	}

	public int getFinalColor() {
		if (!rainbow) return color;
		return Mth.hsvToRgb((Util.getMillis() % 2000L) / 2000.0F, 0.8F, 1.0F);
	}
}
