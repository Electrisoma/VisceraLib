package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.platform.registration.v1.services.INewRegistryHelper;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.google.auto.service.AutoService;

@AutoService(INewRegistryHelper.class)
public class NewRegistryImpl implements INewRegistryHelper {

	@Override
	public <T> Registry<T> createCustomRegistry(
			ResourceKey<Registry<T>> key,
			VisceralRegistrySettings settings
	) {
		FabricRegistryBuilder<T, ?> builder;

		if (settings.defaultId() != null) {
			builder = FabricRegistryBuilder.createDefaulted(key, settings.defaultId());
		} else {
			builder = FabricRegistryBuilder.createSimple(key);
		}

		if (settings.sync())
			builder.attribute(RegistryAttribute.SYNCED);

		return builder.buildAndRegister();
	}

	@Override
	public void onNewRegistry(Object event) {}
}
