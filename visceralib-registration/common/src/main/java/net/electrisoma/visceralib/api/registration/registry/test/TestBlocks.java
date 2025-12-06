package net.electrisoma.visceralib.api.registration.registry.test;

import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.api.registration.registry.holder.BlockHolder;
import net.electrisoma.visceralib.api.registration.registry.test.stuff.TestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

public class TestBlocks {

    public static void init() {}

    public static BlockHolder<TestBlock> BLOCK = Constants.REGISTRY
            .block("test", TestBlock::new)
            .initialProperties(Blocks.DIAMOND_BLOCK)
            .properties(p -> p.strength(1.0F, 10000.0F))
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .register();


}
