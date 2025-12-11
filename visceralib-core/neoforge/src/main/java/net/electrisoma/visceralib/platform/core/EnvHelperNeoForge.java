package net.electrisoma.visceralib.platform.core;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.IEnvHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

@AutoService(IEnvHelper.class)
public class EnvHelperNeoForge implements IEnvHelper {

    @Override
    public EnvironmentEnum getEnvironmentInfo() {
        return FMLEnvironment.dist ==
                Dist.CLIENT ? EnvironmentEnum.CLIENT : EnvironmentEnum.SERVER;
    }
}
