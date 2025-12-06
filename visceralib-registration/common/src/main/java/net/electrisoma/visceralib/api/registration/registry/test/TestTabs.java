package net.electrisoma.visceralib.api.registration.registry.test;

import net.electrisoma.visceralib.impl.registration.Constants;
import net.electrisoma.visceralib.api.registration.registry.holder.CreativeTabHolder;
import net.minecraft.world.item.ItemStack;

public class TestTabs {

    public static void init() {}

    public static final CreativeTabHolder TEST_TAB = Constants.REGISTRY
            .tab("test_tab")
            .icon(() -> new ItemStack(TestItems.BLOCK_ITEM_H.get()))
            .addStack(() -> new ItemStack(TestBlocks.BLOCK.get()))
            .register();
}
