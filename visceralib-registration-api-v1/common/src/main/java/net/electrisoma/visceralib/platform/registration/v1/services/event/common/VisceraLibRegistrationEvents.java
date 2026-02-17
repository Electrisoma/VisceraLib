package net.electrisoma.visceralib.platform.registration.v1.services.event.common;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.registration.v1.common.CreativeTabRegistrar;
import net.electrisoma.visceralib.event.registration.v1.common.DynamicRegistryRegistrar;
import net.electrisoma.visceralib.event.registration.v1.common.StaticRegistryRegistrar;
import net.electrisoma.visceralib.event.registration.v1.common.VisceralRegistrationHooks;

import java.util.function.Consumer;

public interface VisceraLibRegistrationEvents {

	VisceraLibRegistrationEvents INSTANCE = ServiceHelper.load(VisceraLibRegistrationEvents.class);

	void registerStaticRegistries(VisceralRegistrationHooks.Static handler);
	void registerDynamicRegistries(VisceralRegistrationHooks.Dynamic handler);
	void modifyCreativeTabs(VisceralRegistrationHooks.CreativeTab handler);
}
