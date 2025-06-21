package net.electrisoma.resotech.client;

import net.electrisoma.resotech.api.registration.builders.BlockBuilder;
import net.electrisoma.resotech.multiloader.RenderLayerRegistrar;

import net.createmod.catnip.annotations.Environment;

@Environment(Environment.EnvType.CLIENT)
public class ResoTechClient {
    public static void init() {
        BlockBuilder.getAllBuilders().forEach(builder -> {
            builder.getRenderLayer().ifPresent(renderType -> {
                RenderLayerRegistrar.register(builder.getRegisteredBlock().orElse(null), renderType);
            });
        });
    }
}
