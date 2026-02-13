package net.electrisoma.visceralib.platform.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.platform.registration.v1.services.INewRegistryHelper;

import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.List;

@AutoService(INewRegistryHelper.class)
public class NewRegistryImpl implements INewRegistryHelper {

	private final List<Registry<?>> pending = new ArrayList<>();

	@Override
	public <T> Registry<T> createCustomRegistry(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings) {
		RegistryBuilder<T> builder = new RegistryBuilder<>(key);

		if (settings.defaultId() != null)
			builder.defaultKey(settings.defaultId());

		if (settings.sync())
			builder.sync(true);

		Registry<T> registry = builder.create();
		pending.add(registry);
		return registry;
	}

	@Override
	public void onNewRegistry(Object event) {
		if (event instanceof NewRegistryEvent neoEvent) {
			pending.forEach(neoEvent::register);
			pending.clear();
		}
	}
}
