package net.electrisoma.visceralib.mixin.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluid;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//TODO: something better
@Mixin(Entity.class)
public abstract class EntityFluidPhysicsMixin {

	@Shadow
	public abstract Level level();

	@Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("RETURN"), cancellable = true)
	private void visceral$handleCustomFluidBuoyancy(CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;
		FluidState state = self.level().getFluidState(self.blockPosition());

		if (state.getType() instanceof VisceralFluid vf) {
			if (vf.getAttributes().canSwim()) {
				cir.setReturnValue(true);
			}
		}
	}
}
