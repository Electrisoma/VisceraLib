package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.event.registration.v1.common.CreativeTabEvents;
import net.minecraft.world.item.Items;

public final class TestSetup {

    public static void init() {
        TestCreativeTabs.init();
        TestRegistry.init();

        CreativeTabEvents.modifyEntries(entries -> {
            entries.add(TestCreativeTabs.NORMAL_TAB.key(), () -> Items.NETHERITE_INGOT);
            entries.add(CreativeTabEvents.getTabKey("minecraft", "ingredients"), () -> Items.DRAGON_EGG);
            entries.add(TestCreativeTabs.BUILDER_TAB.key(), () -> Items.DIAMOND_BLOCK);
        });
    }
}