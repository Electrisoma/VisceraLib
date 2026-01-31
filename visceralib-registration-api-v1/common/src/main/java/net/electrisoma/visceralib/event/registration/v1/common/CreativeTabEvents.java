package net.electrisoma.visceralib.event.registration.v1.common;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.event.core.common.IVisceralListener;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabFiller;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;
import java.util.function.Supplier;

@AutoService(IVisceralListener.class)
public class CreativeTabEvents implements IVisceralListener {

    @Override
    public void register() {
        ITabFiller.INSTANCE.registerListeners();
    }

    public static void modifyEntries(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> item) {
        ITabFiller.INSTANCE.addBinding(tab, item);
    }

    public static void modifyEntries(Consumer<BindingCollector> consumer) {
        consumer.accept(ITabFiller.INSTANCE::addBinding);
    }

    public static ResourceKey<CreativeModeTab> getTabKey(String namespace, String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, RLUtils.path(namespace, path));
    }

    @FunctionalInterface
    public interface BindingCollector {
        void add(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> item);
    }
}