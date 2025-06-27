package net.electrisoma.testmod;

import net.electrisoma.testmod.registry.ModSetup;
import net.electrisoma.testmod.registry.TestModTabs;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.electrisoma.visceralib.multiloader.VisceralRegistrySetup;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class TestMod {
    public static final String MOD_ID = "testmod";
    public static final String NAME = "TestMod";
    public static final String VERSION = PlatformInfo.findVersion();
    public static final PlatformInfo LOADER = PlatformInfo.getCurrent();
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final VisceralRegistrar REGISTRAR = VisceralRegistrar.create(MOD_ID)
            .defaultCreativeTab(() -> TestModTabs.BASE);

    public static void init() {
        LOGGER.info("{} {} initializing! on platform: {}", NAME, VERSION, LOADER);

        VisceralRegistrySetup.init();

        ModSetup.register();
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static VisceralRegistrar registrar() {
        return REGISTRAR;
    }
}
