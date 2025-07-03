package net.electrisoma.visceralib.client;

import net.electrisoma.visceralib.annotations.Env;

@Env(Env.EnvType.CLIENT)
public class VisceralLibClient {
    public static void init() {
//        BlockBuilder.getAllBuilders().forEach(builder -> {
//            builder.getRenderLayer().ifPresent(renderType -> {
//                RenderLayerRegistrar.register(builder.getRegisteredBlock().orElse(null), renderType);
//            });
//        });
    }
}
