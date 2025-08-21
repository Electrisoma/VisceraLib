package net.electrisoma.visceralib.fabric;

import net.electrisoma.visceralib.VisceraLib;

import net.fabricmc.api.ModInitializer;

public class VisceraLibImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		VisceraLib.init();
	}
}
