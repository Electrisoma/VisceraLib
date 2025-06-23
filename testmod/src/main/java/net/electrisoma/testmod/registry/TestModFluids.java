package net.electrisoma.testmod.registry;

import net.electrisoma.testmod.TestMod;
import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.FluidEntry;
import net.electrisoma.visceralib.core.fluid.VisceralFlowingFluid;
import net.minecraft.tags.FluidTags;

@SuppressWarnings("unused")
public class TestModFluids {
    private static final VisceralRegistrar REGISTRAR = TestMod.registrar();

    public static void init() {
        TestMod.LOGGER.info("Registering Fluids for " + TestMod.NAME);
    }

    public static final FluidEntry<VisceralFlowingFluid.Flowing> TEST_FLUID = REGISTRAR
            .fluid("test_fluid")
            .lang("Test Fluid")
            .tags(FluidTags.WATER)
            .tab(TestModTabs.MACHINES::getKey)
            .register();
}
