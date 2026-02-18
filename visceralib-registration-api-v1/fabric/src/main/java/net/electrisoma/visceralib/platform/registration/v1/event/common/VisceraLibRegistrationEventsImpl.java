package net.electrisoma.visceralib.platform.registration.v1.event.common;

import net.electrisoma.visceralib.api.registration.v1.registry.register.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.event.registration.v1.common.DynamicRegistryRegistrar;
import net.electrisoma.visceralib.event.registration.v1.common.StaticRegistryRegistrar;
import net.electrisoma.visceralib.event.registration.v1.common.VisceralRegistrationHooks;
import net.electrisoma.visceralib.platform.registration.v1.services.event.common.VisceraLibRegistrationEvents;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.Nullable;

@AutoService(VisceraLibRegistrationEvents.class)
public final class VisceraLibRegistrationEventsImpl implements VisceraLibRegistrationEvents {

	@Override
	public void registerStaticRegistries(VisceralRegistrationHooks.Static handler) {
		handler.onRegister(new StaticRegistryRegistrar() {

			@Override
			public <T> Registry<T> register(ResourceKey<Registry<T>> key, VisceralRegistrySettings settings) {
				FabricRegistryBuilder<T, ?> builder = settings.defaultId() != null
						? FabricRegistryBuilder.createDefaulted(key, settings.defaultId())
						: FabricRegistryBuilder.createSimple(key);

				if (settings.sync()) builder.attribute(RegistryAttribute.SYNCED);

				return builder.buildAndRegister();
			}
		});
	}

	@Override
	public void registerDynamicRegistries(VisceralRegistrationHooks.Dynamic handler) {
		handler.onRegister(new DynamicRegistryRegistrar() {

			@Override
			public <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, @Nullable Codec<T> net) {
				if (net != null) {
					DynamicRegistries.registerSynced(key, codec, net);
				} else {
					DynamicRegistries.register(key, codec);
				}
			}
		});
	}

	@Override
	public void modifyCreativeTabs(VisceralRegistrationHooks.CreativeTab handler) {
		handler.onModify((tabKey, stack, visibility) ->
				ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries ->
						entries.accept(stack, visibility))
		);
	}
}
