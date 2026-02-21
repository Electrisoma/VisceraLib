package net.electrisoma.visceralib.platform.registration.v1.services.event.common;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.registration.v1.common.CreativeTabEvents;
import net.electrisoma.visceralib.event.registration.v1.common.EntityEvents;
import net.electrisoma.visceralib.event.registration.v1.common.RegistryRegistrationEvents;

public interface VisceraLibRegistrationEvents {

	VisceraLibRegistrationEvents INSTANCE = ServiceHelper.load(VisceraLibRegistrationEvents.class);

	void registerStaticRegistries(RegistryRegistrationEvents.Static handler);
	void registerDynamicRegistries(RegistryRegistrationEvents.Dynamic handler);

	void modifyCreativeTabs(CreativeTabEvents.ModifyTab handler);

	void registerAttributes(EntityEvents.Attributes handler);
}
