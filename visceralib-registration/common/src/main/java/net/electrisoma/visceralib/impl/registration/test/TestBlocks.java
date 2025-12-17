package net.electrisoma.visceralib.impl.registration.test;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.RegistryObject;
import net.electrisoma.visceralib.impl.registration.test.stuff.TestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

public class TestBlocks {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static RegistryObject<TestBlock> BLOCK = REGISTRY
            .block("test", TestBlock::new)
            .initialProperties(Blocks.DIAMOND_BLOCK)
            .properties(p -> p.strength(1.0F, 10000.0F))
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .register();

    public static void init() {}
}
