package net.electrisoma.visceralib.client;

import net.electrisoma.visceralib.annotations.Env;
import net.electrisoma.visceralib.multiloader.RenderLayerRegistrar;

@Env(Env.EnvType.CLIENT)
public class ResoTechClient {
    public static void init() {
//        BlockBuilder.getAllBuilders().forEach(builder -> {
//            builder.getRenderLayer().ifPresent(renderType -> {
//                RenderLayerRegistrar.register(builder.getRegisteredBlock().orElse(null), renderType);
//            });
//        });
    }
}
