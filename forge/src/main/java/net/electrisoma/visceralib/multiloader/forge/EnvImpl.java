package net.electrisoma.visceralib.multiloader.forge;

import net.electrisoma.visceralib.multiloader.Env;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

@SuppressWarnings("unused")
public class EnvImpl {
    public static Env getCurrent() {
        return FMLEnvironment.dist == Dist.CLIENT ? Env.CLIENT : Env.SERVER;
    }
}