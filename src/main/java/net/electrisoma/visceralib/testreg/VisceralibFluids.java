package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.BlockEntry;
import net.electrisoma.visceralib.api.registration.entry.FluidEntry;
import net.electrisoma.visceralib.api.registration.helpers.SharedProperties;
import net.electrisoma.visceralib.core.fluid.VisceralFlowingFluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import static net.electrisoma.visceralib.api.registration.helpers.BuilderTransforms.pickaxeOnly;

public class VisceralibFluids {
    public static void init() {
        VisceraLib.LOGGER.info("Registering Blocks for " + VisceraLib.NAME);
    }

    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static final FluidEntry<VisceralFlowingFluid.Flowing> TEST_FLUID = REGISTRAR
            .fluid("test_fluid")
            .textures(
                    VisceraLib.path(VisceraLib.MOD_ID, "test_fluid_still"),
                    VisceraLib.path(VisceraLib.MOD_ID,"test_fluid_flow")
            )
            .lang("Test Fluid")
            .withBlock(block -> block
                    .properties(props -> props
                            .strength(100.0F)
                            .noLootTable()
                            .liquid()
                    )
            )
            .withBucket()
            .register();
}
