package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistrationHelper;
import net.electrisoma.visceralib.impl.registration.v1.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

public final class TestCreativeTabs {

    public static final VisceralRegistrationHelper REGISTRY = Constants.registry();

    public static final RegistryObject<CreativeModeTab> TEST_TAB = REGISTRY.autoTab(
            "test_tab", builder -> builder
                    .title(Component.literal("test_tab"))
                    .icon(() -> new ItemStack(TestRegistry.TEST_ITEM.get()))
    );

    public static void init() {}
}