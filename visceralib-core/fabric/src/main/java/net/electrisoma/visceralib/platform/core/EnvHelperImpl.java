package net.electrisoma.visceralib.platform.core;

import net.electrisoma.visceralib.platform.core.services.IEnvHelper;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import com.google.auto.service.AutoService;

@AutoService(IEnvHelper.class)
public final class EnvHelperImpl implements IEnvHelper {

	@Override
	public EnvironmentEnum getEnvironmentInfo() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
				? EnvironmentEnum.CLIENT : EnvironmentEnum.SERVER;
	}
}
