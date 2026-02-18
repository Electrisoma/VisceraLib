package net.electrisoma.visceralib.platform.registration.v1.event.common;

import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.event.registration.v1.common.StaticRegistryRegistrar;
import net.electrisoma.visceralib.event.registration.v1.common.VisceralRegistrationHooks;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.registration.v1.services.event.common.VisceraLibRegistrationEvents;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.google.auto.service.AutoService;

import java.util.function.Consumer;

@AutoService(VisceraLibRegistrationEvents.class)
public final class VisceraLibRegistrationEventsImpl implements VisceraLibRegistrationEvents {

	@Override
	public void registerStaticRegistries(VisceralRegistrationHooks.Static handler) {
		withBus(bus -> bus.addListener((NewRegistryEvent event) ->
			handler.onRegister(new StaticRegistryRegistrar() {

				@Override
				public <T> Registry<T> register(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings) {
					RegistryBuilder<T> builder = new RegistryBuilder<>(key);
					if (settings.defaultId() != null) builder.defaultKey(settings.defaultId());
					if (settings.sync()) builder.sync(true);

					Registry<T> registry = builder.create();
					event.register(registry);
					return registry;
				}
			})
		));
	}

	@Override
	public void registerDynamicRegistries(VisceralRegistrationHooks.Dynamic handler) {
		withBus(bus -> bus.addListener((DataPackRegistryEvent.NewRegistry event) ->
				handler.onRegister(event::dataPackRegistry)
		));
	}

	@Override
	public void modifyCreativeTabs(VisceralRegistrationHooks.CreativeTab handler) {
		withBus(bus -> bus.addListener((BuildCreativeModeTabContentsEvent event) ->
			handler.onModify((tabKey, stack, visibility) -> {
				if (event.getTabKey().equals(tabKey))
					event.accept(stack, visibility);
			})
		));
	}

	private void withBus(Consumer<IEventBus> consumer) {
		IEventBus modBus = IPlatformHelper.INSTANCE.getModEventBus();
		if (modBus != null) consumer.accept(modBus);
	}
}
