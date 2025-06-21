package net.electrisoma.resotech.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class RenderLayerRegistrar {
    @ExpectPlatform
    public static void register(Block block, RenderType renderType) {
        throw new AssertionError();
    }
}
