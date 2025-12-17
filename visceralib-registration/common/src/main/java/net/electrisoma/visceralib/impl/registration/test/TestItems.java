package net.electrisoma.visceralib.impl.registration.test;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.RegistryObject;
import net.electrisoma.visceralib.impl.registration.test.stuff.TestBlock;

public class TestItems {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static final RegistryObject<TestBlock.TestBlockItem> BLOCK_ITEM_H = REGISTRY
            .item("test_h", TestBlock.TestBlockItem::new)
            .register();

    public static void init() {}
}
