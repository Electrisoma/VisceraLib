package net.electrisoma.visceralib.impl.registration.test;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.RegistryObject;
import net.electrisoma.visceralib.impl.registration.test.stuff.TestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TestBlockEntities {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY = REGISTRY
            .blockEntity("test_be", TestBlockEntity::new)
            .validBlocks(TestBlocks.BLOCK::get)
            .register();

    public static void init() {}
}
