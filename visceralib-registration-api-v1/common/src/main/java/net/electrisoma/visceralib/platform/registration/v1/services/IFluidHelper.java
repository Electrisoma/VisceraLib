package net.electrisoma.visceralib.platform.registration.v1.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.v1.registry.fluid.VisceralFluidAttributes;

public interface IFluidHelper {

	IFluidHelper INSTANCE = ServiceHelper.load(IFluidHelper.class);

	void registerFluidType(String name, VisceralFluidAttributes attributes, VisceralRegistry registry);
}
