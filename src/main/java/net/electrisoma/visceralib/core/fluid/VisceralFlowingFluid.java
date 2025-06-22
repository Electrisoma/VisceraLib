package net.electrisoma.visceralib.core.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class VisceralFlowingFluid extends FlowingFluid {
    protected final VisceralFluidAttributes attributes;

    protected VisceralFlowingFluid(VisceralFluidAttributes attributes) {
        this.attributes = attributes;
    }

    public VisceralFluidAttributes getAttributes() {
        return attributes;
    }

    public static class Source extends VisceralFlowingFluid {
        public Source(VisceralFluidAttributes attributes) {
            super(attributes);
        }

        @Override
        public Item getBucket() {
            return null;
        }

        @Override
        protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
            return false;
        }

        @Override
        public int getTickDelay(LevelReader level) {
            return 0;
        }

        @Override
        protected float getExplosionResistance() {
            return 0;
        }

        @Override
        protected BlockState createLegacyBlock(FluidState state) {
            return null;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public Fluid getFlowing() {
            return null;
        }

        @Override
        public Fluid getSource() {
            return null;
        }

        @Override
        protected boolean canConvertToSource(Level level) {
            return false;
        }

        @Override
        protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {

        }

        @Override
        protected int getSlopeFindDistance(LevelReader level) {
            return 0;
        }

        @Override
        protected int getDropOff(LevelReader level) {
            return 0;
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
    }

    public static class Flowing extends VisceralFlowingFluid {
        public Flowing(VisceralFluidAttributes attributes) {
            super(attributes);
        }

        @Override
        public Item getBucket() {
            return null;
        }

        @Override
        protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
            return false;
        }

        @Override
        public int getTickDelay(LevelReader level) {
            return 0;
        }

        @Override
        protected float getExplosionResistance() {
            return 0;
        }

        @Override
        protected BlockState createLegacyBlock(FluidState state) {
            return null;
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public Fluid getFlowing() {
            return null;
        }

        @Override
        public Fluid getSource() {
            return null;
        }

        @Override
        protected boolean canConvertToSource(Level level) {
            return false;
        }

        @Override
        protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {

        }

        @Override
        protected int getSlopeFindDistance(LevelReader level) {
            return 0;
        }

        @Override
        protected int getDropOff(LevelReader level) {
            return 0;
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
    }
}
