package net.electrisoma.visceralib.api.registration.fluid;
import java.util.function.Supplier;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;

public class VisceralLiquidBlock extends LiquidBlock {
    public VisceralLiquidBlock(Supplier<? extends FlowingFluid> fluidSupplier, BlockBehaviour.Properties properties) {
        super(fluidSupplier.get(), properties);
    }
}
