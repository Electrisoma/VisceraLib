package net.electrisoma.visceralib.api.registration.v1.registry.register.fluid;

import net.electrisoma.visceralib.api.registration.v1.registry.VisceralRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class VisceralFluid extends FlowingFluid {

    private final VisceralFluidProperties props;
    private final VisceralRegistryHelper helper;

    protected VisceralFluid(VisceralFluidProperties props, VisceralRegistryHelper helper) {
        this.props = props;
        this.helper = helper;
    }

    @Override
    public Fluid getFlowing() {
        return props.flowing().get(helper);
    }

    @Override
    public Fluid getSource() {
        return props.still().get(helper);
    }

    @Override
    public Item getBucket() {
        return props.bucket().get(helper);
    }

    @Override
    public int getSlopeFindDistance(LevelReader level) {
        return props.slopeFindDistance();
    }

    @Override
    public int getDropOff(LevelReader level) {
        return props.dropOff();
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return props.tickDelay();
    }

    @Override
    protected float getExplosionResistance() {
        return props.explosionResistance();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return props.canMultiply();
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        Block block = props.block().get(helper);
        if (block instanceof LiquidBlock)
            return block.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
        return block.defaultBlockState();
    }

    @Override
    protected boolean canBeReplacedWith(
            FluidState state,
            BlockGetter level,
            BlockPos pos,
            Fluid fluid,
            Direction direction
    ) {
        return direction == Direction.DOWN || fluid.isSame(this);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    public int getAmount(FluidState state) {
        return isSource(state) ? 8 : state.getValue(LEVEL);
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == getSource() || fluid == getFlowing();
    }

    public VisceralFluidAttributes getAttributes() {
        return props.attributes();
    }

    public static class Flowing extends VisceralFluid {

        public Flowing(VisceralFluidProperties props, VisceralRegistryHelper helper) {
            super(props, helper);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends VisceralFluid {

        public Source(VisceralFluidProperties props, VisceralRegistryHelper helper) {
            super(props, helper);
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}