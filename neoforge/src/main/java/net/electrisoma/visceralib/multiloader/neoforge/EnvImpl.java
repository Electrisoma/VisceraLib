package net.electrisoma.visceralib.multiloader.neoforge;

import net.electrisoma.visceralib.multiloader.Env;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

@SuppressWarnings("unused")
public class EnvImpl {
    public static Env getCurrent() {
        return FMLEnvironment.dist == Dist.CLIENT ? Env.CLIENT : Env.SERVER;
    }
}