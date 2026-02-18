package net.electrisoma.visceralib.event.registration.v1.common;

public final class VisceralRegistrationHooks {

	private VisceralRegistrationHooks() {}

	@FunctionalInterface
	public interface Static {

		void onRegister(StaticRegistryRegistrar registrar);
	}

	@FunctionalInterface
	public interface Dynamic {

		void onRegister(DynamicRegistryRegistrar registrar);
	}

	@FunctionalInterface
	public interface CreativeTab {

		void onModify(CreativeTabRegistrar registrar);
	}
}
