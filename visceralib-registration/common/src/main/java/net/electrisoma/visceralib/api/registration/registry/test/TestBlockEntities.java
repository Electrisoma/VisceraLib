package net.electrisoma.visceralib.api.registration.registry.test;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.RegistryObject;
import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.api.registration.registry.holder.BlockEntityHolder;
import net.electrisoma.visceralib.api.registration.registry.test.stuff.TestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TestBlockEntities {

    public static VisceralRegistry REGISTRY = new VisceralRegistry(Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY = REGISTRY
            .blockEntity("test_be", TestBlockEntity::new)
            .validBlocks(TestBlocks.BLOCK::get)
            .register();

    public static void init() {}
}
