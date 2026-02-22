package net.electrisoma.visceralib.api.registration.v1.registry.fluid;

import net.electrisoma.visceralib.api.registration.v1.registry.helper.VisceralRegistryHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

public interface VisceralFluidProperties {

	@FunctionalInterface
	interface FluidLazy<T> {

		T get(VisceralRegistryHelper helper);
	}

	FluidLazy<? extends FlowingFluid> still();
	FluidLazy<? extends FlowingFluid> flowing();
	FluidLazy<? extends Block> block();
	FluidLazy<? extends Item> bucket();
	VisceralFluidAttributes attributes();
	int slopeFindDistance();
	int dropOff();
	int tickDelay();
	float explosionResistance();
	boolean canMultiply();

	class Builder {

		private final FluidLazy<? extends FlowingFluid> still;
		private final FluidLazy<? extends FlowingFluid> flowing;
		private FluidLazy<? extends Block> block;
		private FluidLazy<? extends Item> bucket;
		private VisceralFluidAttributes attributes = new VisceralFluidAttributes.Builder().build();
		private int slopeFindDistance = 4;
		private int dropOff = 1;
		private int tickDelay = 5;
		private float explosionResistance = 100.0f;
		private boolean canMultiply = false;

		public Builder(FluidLazy<? extends FlowingFluid> still, FluidLazy<? extends FlowingFluid> flowing) {
			this.still = still;
			this.flowing = flowing;
		}

		public Builder block(FluidLazy<? extends Block> block) {
			this.block = block;
			return this;
		}

		public Builder bucket(FluidLazy<? extends Item> bucket) {
			this.bucket = bucket;
			return this;
		}

		public Builder attributes(VisceralFluidAttributes attributes) {
			this.attributes = attributes;
			return this;
		}

		public Builder slope(int slope) {
			this.slopeFindDistance = slope;
			return this;
		}

		public Builder dropOff(int dropOff) {
			this.dropOff = dropOff;
			return this;
		}

		public Builder tickDelay(int delay) {
			this.tickDelay = delay;
			return this;
		}

		public Builder explosionResistance(float resistance) {
			this.explosionResistance = resistance;
			return this;
		}

		public Builder canMultiply() {
			this.canMultiply = true;
			return this;
		}

		public VisceralFluidProperties build() {

			record Data(
					FluidLazy<? extends FlowingFluid> still,
					FluidLazy<? extends FlowingFluid> flowing,
					FluidLazy<? extends Block> block,
					FluidLazy<? extends Item> bucket,
					VisceralFluidAttributes attributes,
					int slopeFindDistance,
					int dropOff,
					int tickDelay,
					float explosionResistance,
					boolean canMultiply
			) implements VisceralFluidProperties {}

			return new Data(
					still,
					flowing,
					block,
					bucket,
					attributes,
					slopeFindDistance,
					dropOff,
					tickDelay,
					explosionResistance,
					canMultiply
			);
		}
	}
}
