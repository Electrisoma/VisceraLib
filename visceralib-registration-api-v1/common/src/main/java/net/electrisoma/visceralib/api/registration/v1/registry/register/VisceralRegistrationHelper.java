package net.electrisoma.visceralib.api.registration.v1.registry.register;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class VisceralRegistrationHelper extends AbstractRegistrationHelper<VisceralRegistrationHelper> {

    private RegistryObject<CreativeModeTab> activeTab;
    private final Map<RegistryObject<CreativeModeTab>, List<RegistryObject<? extends Item>>> tabMap = new ConcurrentHashMap<>();

    private VisceralRegistrationHelper(String modId) {
        super(new VisceralRegistry(modId));
    }

    public static VisceralRegistrationHelper of(String modId) {
        return new VisceralRegistrationHelper(modId);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <R, T extends R> RegistryObject<T> register(Registry<R> reg, String name, Supplier<T> supplier) {
        RegistryObject<T> obj = super.register(reg, name, supplier);

        if (activeTab != null && reg.key().equals(Registries.ITEM)) {
            tabMap.computeIfAbsent(activeTab, k -> new CopyOnWriteArrayList<>())
                    .add((RegistryObject<? extends Item>) obj);
        }

        return obj;
    }

    public void withTab(RegistryObject<CreativeModeTab> tab) {
        this.activeTab = tab;
    }

    @Override
    public RegistryObject<CreativeModeTab> autoTab(String name, Consumer<CreativeModeTab.Builder> builderConfig) {
        ResourceLocation tabId = RLUtils.path(registry.modId(), name);

        return tab(name, () -> ITabHelper.INSTANCE.create(builder -> {
            builderConfig.accept(builder);

            builder.displayItems((params, output) ->
                    tabMap.forEach((handle, list) -> {
                        if (handle.key().location().equals(tabId)) {
                            list.forEach(item -> output.accept(item.get()));
                        }
                    })
            );
        }));
    }
}