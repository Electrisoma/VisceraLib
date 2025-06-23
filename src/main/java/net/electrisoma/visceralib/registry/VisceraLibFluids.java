package net.electrisoma.visceralib.registry;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;

import net.electrisoma.visceralib.api.registration.entry.FluidEntry;
import net.electrisoma.visceralib.core.fluid.VisceralFlowingFluid;
import net.minecraft.tags.FluidTags;

@SuppressWarnings("unused")
public class VisceraLibFluids {
    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static void init() {
        VisceraLib.LOGGER.info("Registering Fluids for " + VisceraLib.NAME);
    }

    public static final FluidEntry<VisceralFlowingFluid.Flowing> TEST_FLUID = REGISTRAR
            .fluid("test_fluid")
            .lang("Test Fluid")
            .tags(FluidTags.WATER)
            .tab(VisceraLibTabs.MACHINES::getKey)
            .register();
}
