package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.v1.registry.register.fluid.VisceralFluidAttributes;
import net.electrisoma.visceralib.platform.registration.v1.services.IFluidHelper;

import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.minecraft.sounds.SoundEvents;

import com.google.auto.service.AutoService;

@AutoService(IFluidHelper.class)
public class FluidHelperImpl implements IFluidHelper {

	@Override
	public void registerFluidType(String name, VisceralFluidAttributes attributes, VisceralRegistry registry) {

		FluidType.Properties props = FluidType.Properties.create()
				.descriptionId("fluid." + registry.modId() + "." + name)
				.density(attributes.density())
				.viscosity(attributes.viscosity())
				.temperature(attributes.temperature())
				.lightLevel(attributes.lightLevel())
				.rarity(attributes.rarity())
				.motionScale(attributes.motionScale())
				.canSwim(attributes.canSwim())
				.canPushEntity(attributes.canPushEntity())
				.canDrown(attributes.canDrown())
				.fallDistanceModifier(attributes.fallDistanceModifier())
				.pathType(attributes.pathType())
				.adjacentPathType(attributes.adjacentPathType())
				.sound(SoundActions.BUCKET_FILL, attributes.sounds().fill())
				.sound(SoundActions.BUCKET_EMPTY, attributes.sounds().empty())
				.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH);

		registry.register(NeoForgeRegistries.FLUID_TYPES, name, () -> new FluidType(props));
	}
}
