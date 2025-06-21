package net.electrisoma.resotech.registry;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.builders.FluidBuilder;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

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
            new FluidBuilder("test_fluid", FLUIDS, ResoTechItems.ITEMS, ResoTechBlocks.BLOCKS)
                    .lang("Test Fluid")
                    .fogColor(new Vec3(1, 0, 0))
                    .fogDensity(8)
                    .tags(AllFluidTags.TEST.tag)
                    .tab(ResoTechTabs.BASE)
                    .register();
}
