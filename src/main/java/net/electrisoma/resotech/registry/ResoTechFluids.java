package net.electrisoma.resotech.registry;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.FluidBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public class ResoTechFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ResoTech.MOD_ID, Registries.FLUID);

    public static final List<ArchitecturyFluidAttributes> FLUIDS_INFOS = new ArrayList<>();

//    public static final ArchitecturyFluidAttributes TEST_ATTRIBUTES =
//            SimpleArchitecturyFluidAttributes.ofSupplier(() ->
//                            ResoTechFluids.TEST_FLOW, () ->
//                            ResoTechFluids.TEST_STILL)
//            .blockSupplier(() -> ResoTechBlocks.TEST_FLUID)
//            .slopeFindDistance(4)
//            .dropOff(1)
//            .tickDelay(8)
//            .explosionResistance(100)
//            .lighterThanAir(true)
//            .convertToSource(false)
//            .sourceTexture(path("fluids/test_still"))
//            .flowingTexture(path("fluids/test_flow"));
//
//    public static final RegistrySupplier<FlowingFluid> TEST_FLOW =
//            FLUIDS.register("flowing_test", () ->
//                    new ArchitecturyFlowingFluid.Flowing(TEST_ATTRIBUTES));
//
//    public static final RegistrySupplier<FlowingFluid> TEST_STILL =
//            FLUIDS.register("test", () ->
//                    new ArchitecturyFlowingFluid.Source(TEST_ATTRIBUTES));

    public static final RegistrySupplier<FlowingFluid> TEST_FLUID =
            new FluidBuilder("test", FLUIDS, ResoTechItems.ITEMS, ResoTechBlocks.BLOCKS, FLUIDS_INFOS)
                    .lang("Test Fluid")
                    .tag(FluidTags.WATER)
                    .convertToSource(true)
                    .lighterThanAir(false)
                    .tab(ResoTechTabs.BASE)
                    .register();

    public static void init() {
        FLUIDS.register();
        ResoTech.LOGGER.info("Registering Fluids for " + ResoTech.NAME);
    }
}
