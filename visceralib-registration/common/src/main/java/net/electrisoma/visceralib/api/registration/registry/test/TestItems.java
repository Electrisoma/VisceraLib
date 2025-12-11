package net.electrisoma.visceralib.api.registration.registry.test;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.RegistryObject;
import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.api.registration.registry.holder.ItemHolder;
import net.electrisoma.visceralib.api.registration.registry.test.stuff.TestBlock;

public class TestItems {

    public static VisceralRegistry REGISTRY = new VisceralRegistry(Constants.MOD_ID);

    public static final RegistryObject<TestBlock.TestBlockItem> BLOCK_ITEM_H = REGISTRY
            .item("test_h", TestBlock.TestBlockItem::new)
            .register();

    public static void init() {}
}
