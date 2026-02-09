package net.electrisoma.visceralib.api.registration.v1.registry;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabFiller;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

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
            tryAddBinding(obj);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private <T> void tryAddBinding(RegistryObject<T> obj) {
        RegistryObject<Item> itemObj = (RegistryObject<Item>) obj;
        ITabFiller.INSTANCE.addBinding(activeTab.key(), itemObj::get);
    }
}