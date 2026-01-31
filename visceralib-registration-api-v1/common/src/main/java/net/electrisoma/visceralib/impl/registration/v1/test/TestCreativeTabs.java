package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.VisceralRegistrationHelper;
import net.electrisoma.visceralib.impl.registration.v1.Constants;
import net.electrisoma.visceralib.impl.registration.v1.test.helper.TestRegistrationHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

public final class TestCreativeTabs {

    public static final VisceralRegistrationHelper NORMAL = Constants.registry();
    public static final TestRegistrationHelper TEST_REGISTRY = Constants.testRegistry();

    public static final RegistryObject<CreativeModeTab> NORMAL_TAB = NORMAL.autoTab(
            "normal_tab", builder -> builder
                    .title(Component.literal("normal_tab"))
                    .icon(() -> new ItemStack(TestRegistry.NORMAL_ITEM.get()))
    );

    public static final RegistryObject<CreativeModeTab> BUILDER_TAB = TEST_REGISTRY.autoTab(
            "builder_tab", builder -> builder
                    .title(Component.literal("builder_tab"))
                    .icon(() -> new ItemStack(TestRegistry.BUILDER_ITEM.get()))
    );

    public static void init() {}
}