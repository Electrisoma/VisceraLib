package net.electrisoma.visceralib.api.registration.v1.registry.fluid;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.PathType;

public interface VisceralFluidAttributes {

	double motionScale();
	boolean canPushEntity();
	boolean canSwim();
	boolean canDrown();
	float fallDistanceModifier();
	boolean canExtinguish();
	boolean canConvertToSource();
	boolean supportsBoating();
	PathType pathType();
	PathType adjacentPathType();
	boolean canHydrate();
	int lightLevel();
	int density();
	int temperature();
	int viscosity();
	Rarity rarity();
	FluidSounds sounds();

	record FluidSounds(
			SoundEvent fill,
			SoundEvent empty,
			SoundEvent ambient
	) {
		public static final FluidSounds WATER = new FluidSounds(
				SoundEvents.BUCKET_FILL,
				SoundEvents.BUCKET_EMPTY,
				SoundEvents.AMBIENT_UNDERWATER_LOOP
		);
	}

	class Builder {

		private double motionScale = 0.014D;
		private boolean canPushEntity = true;
		private boolean canSwim = true;
		private boolean canDrown = true;
		private float fallDistanceModifier = 0.5F;
		private boolean canExtinguish = false;
		private boolean canConvertToSource = false;
		private boolean supportsBoating = false;
		private PathType pathType = PathType.WATER;
		private PathType adjacentPathType = PathType.WATER_BORDER;
		private boolean canHydrate = false;
		private int lightLevel = 0;
		private int density = 1000;
		private int temperature = 300;
		private int viscosity = 1000;
		private Rarity rarity = Rarity.COMMON;
		private FluidSounds sounds = FluidSounds.WATER;

		public Builder motionScale(double scale) {
			this.motionScale = scale;
			return this;
		}

		public Builder canSwim(boolean canSwim) {
			this.canSwim = canSwim;
			return this;
		}

		public Builder canDrown(boolean canDrown) {
			this.canDrown = canDrown;
			return this;
		}

		public Builder canExtinguish() {
			this.canExtinguish = true;
			return this;
		}

		public Builder canConvertToSource() {
			this.canConvertToSource = true;
			return this;
		}

		public Builder lightLevel(int light) {
			this.lightLevel = light;
			return this;
		}

		public Builder density(int density) {
			this.density = density;
			return this;
		}

		public Builder temperature(int temp) {
			this.temperature = temp;
			return this;
		}

		public Builder viscosity(int visc) {
			this.viscosity = visc;
			return this;
		}

		public Builder sounds(FluidSounds sounds) {
			this.sounds = sounds;
			return this;
		}

		public VisceralFluidAttributes build() {
			record Data(
					double motionScale,
					boolean canPushEntity,
					boolean canSwim,
					boolean canDrown,
					float fallDistanceModifier,
					boolean canExtinguish,
					boolean canConvertToSource,
					boolean supportsBoating,
					PathType pathType,
					PathType adjacentPathType,
					boolean canHydrate,
					int lightLevel,
					int density,
					int temperature,
					int viscosity,
					Rarity rarity,
					FluidSounds sounds
			) implements VisceralFluidAttributes {}

			return new Data(
					motionScale,
					canPushEntity,
					canSwim,
					canDrown,
					fallDistanceModifier,
					canExtinguish,
					canConvertToSource,
					supportsBoating,
					pathType,
					adjacentPathType,
					canHydrate,
					lightLevel,
					density,
					temperature,
					viscosity,
					rarity,
					sounds
			);
		}
	}
}
