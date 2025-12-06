package net.electrisoma.visceralib.api.registration.registry.test;

import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.api.registration.registry.holder.ItemHolder;
import net.electrisoma.visceralib.api.registration.registry.test.stuff.TestBlock;

public class TestItems {

    public static void init() {}

    public static final ItemHolder<TestBlock.TestBlockItem> BLOCK_ITEM_H = Constants.REGISTRY
            .item("test_h", TestBlock.TestBlockItem::new)
            .register();
}
