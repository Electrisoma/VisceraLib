package net.electrisoma.visceralib.platform.registration.v1.event.common;

import net.electrisoma.visceralib.api.registration.v1.registry.custom.VisceralRegistrySettings;
import net.electrisoma.visceralib.event.registration.v1.common.CreativeTabEvents;
import net.electrisoma.visceralib.event.registration.v1.common.EntityEvents;
import net.electrisoma.visceralib.event.registration.v1.common.RegistryRegistrationEvents;
import net.electrisoma.visceralib.platform.registration.v1.services.event.common.VisceraLibRegistrationEvents;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.mojang.serialization.Codec;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.Nullable;

@AutoService(VisceraLibRegistrationEvents.class)
public final class VisceraLibRegistrationEventsImpl implements VisceraLibRegistrationEvents {

	@Override
	public void registerStaticRegistries(RegistryRegistrationEvents.Static handler) {
		handler.onRegister(new RegistryRegistrationEvents.StaticRegistrar() {

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
	public void registerDynamicRegistries(RegistryRegistrationEvents.Dynamic handler) {
		handler.onRegister(new RegistryRegistrationEvents.DynamicRegistrar() {

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
	public void modifyCreativeTabs(CreativeTabEvents.ModifyTab handler) {
		handler.register((tabKey, stack, visibility) ->
				ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries ->
						entries.accept(stack, visibility))
		);
	}

	@Override
	public void registerAttributes(EntityEvents.Attributes handler) {
		handler.onRegister(FabricDefaultAttributeRegistry::register);
	}
}
