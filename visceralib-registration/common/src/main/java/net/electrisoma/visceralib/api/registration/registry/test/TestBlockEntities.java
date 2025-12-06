package net.electrisoma.visceralib.api.registration.registry.test;

import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.api.registration.registry.holder.BlockEntityHolder;
import net.electrisoma.visceralib.api.registration.registry.test.stuff.TestBlockEntity;

public class TestBlockEntities {

    public static void init() {}

    public static final BlockEntityHolder<TestBlockEntity> TEST_BLOCK_ENTITY = Constants.REGISTRY
            .blockEntity("test_be", TestBlockEntity::new)
            .validBlocks(TestBlocks.BLOCK::get)
            .register();
}
