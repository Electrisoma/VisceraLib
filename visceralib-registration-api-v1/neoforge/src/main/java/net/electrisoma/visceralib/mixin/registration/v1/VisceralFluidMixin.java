package net.electrisoma.visceralib.mixin.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluid;

import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VisceralFluid.class)
public abstract class VisceralFluidMixin extends Fluid {

	@Override
	public @NotNull FluidType getFluidType() {
		ResourceLocation id = BuiltInRegistries.FLUID.getKey(this);

		if (id.getPath().startsWith("flowing_")) {
			id = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring(8));
		}

		FluidType type = NeoForgeRegistries.FLUID_TYPES.get(id);
		return type != null ? type : NeoForgeMod.WATER_TYPE.value();
	}
}
