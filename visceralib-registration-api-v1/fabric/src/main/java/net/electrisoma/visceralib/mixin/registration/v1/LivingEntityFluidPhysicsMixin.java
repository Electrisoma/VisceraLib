package net.electrisoma.visceralib.mixin.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.fluid.VisceralFluid;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//TODO: not this shit, come up with something better
@Mixin(LivingEntity.class)
public abstract class LivingEntityFluidPhysicsMixin {

	@ModifyVariable(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"), ordinal = 0)
	private double visceral$applyFluidViscosity(double speed) {
		LivingEntity self = (LivingEntity) (Object) this;
		//noinspection resource
		FluidState state = self.level().getFluidState(self.blockPosition());

		if (state.getType() instanceof VisceralFluid vf) {
			float factor = 1000f / Math.max(1, vf.getAttributes().viscosity());
			return speed * factor;
		}

		return speed;
	}
}
