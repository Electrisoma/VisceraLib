package net.electrisoma.visceralib.registry.test;

import net.electrisoma.visceralib.registry.VisceralRegistry;
import net.electrisoma.visceralib.registry.holder.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TestBlockEntityRegistry {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static void init() {}

    public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY = REGISTRY
            .blockEntity("test_be", TestBlockEntity::new)
            .validBlocks(TestRegistry.BLOCK::get)
            .register();
}
