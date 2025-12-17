package net.electrisoma.visceralib.impl.registration.test;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.RegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TestTabs {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static final RegistryObject<CreativeModeTab> TEST_TAB = REGISTRY
            .tab("test_tab")
            .icon(() -> new ItemStack(TestItems.BLOCK_ITEM_H.get()))
            .addStack(() -> new ItemStack(TestBlocks.BLOCK.get()))
            .register();

    public static void init() {}
}
