package net.electrisoma.resotech.registry;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.FluidBuilder;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.tags.FluidTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;

import static net.electrisoma.resotech.registry.ResoTechTags.*;

@SuppressWarnings("unused")
public class ResoTechFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ResoTech.MOD_ID, Registries.FLUID);

    public static void init() {
        FLUIDS.register();
        ResoTech.LOGGER.info("Registering Fluids for " + ResoTech.NAME);
    }

    public static final RegistrySupplier<FlowingFluid> TEST_FLUID =
            new FluidBuilder("test", FLUIDS, ResoTechItems.ITEMS, ResoTechBlocks.BLOCKS)
                    .lang("Test Fluid")
                    .tags(FluidTags.WATER, AllFluidTags.TEST.tag)
                    .tab(ResoTechTabs.BASE)
                    .register();
}
