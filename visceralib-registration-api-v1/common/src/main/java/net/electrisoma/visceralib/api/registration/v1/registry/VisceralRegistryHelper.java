package net.electrisoma.visceralib.api.registration.v1.registry;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.platform.registration.v1.services.event.common.VisceraLibRegistrationEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class VisceralRegistryHelper extends AbstractRegistryHelper {

	private RegistryObject<CreativeModeTab> activeTab;

	public VisceralRegistryHelper(VisceralRegistry registry) {
		super(registry);
	}

	public static VisceralRegistryHelper create(String modId) {
		return new VisceralRegistryHelper(new VisceralRegistry(modId));
	}

	public void withTab(RegistryObject<CreativeModeTab> tab) {
		this.activeTab = tab;
	}

	@Override
	public <R, T extends R> RegistryObject<T> register(Registry<R> reg, String name, Supplier<T> supplier) {
		RegistryObject<T> obj = super.register(reg, name, supplier);
		if (activeTab != null && reg == BuiltInRegistries.ITEM)
			registerToTab(obj);
		return obj;
	}

	@SuppressWarnings("unchecked")
	private <T> void registerToTab(RegistryObject<T> obj) {
		VisceraLibRegistrationEvents.INSTANCE.modifyCreativeTabs(registrar -> {
			Item item = ((RegistryObject<Item>) obj).get();
			registrar.add(activeTab.key(), new ItemStack(item));
		});
	}
}
