package net.electrisoma.visceralib.platform.registration.v1;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabFiller;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@AutoService(ITabFiller.class)
public class TabFillerImpl implements ITabFiller {

    private final Map<ResourceKey<CreativeModeTab>, List<Supplier<? extends Item>>> bindings = new ConcurrentHashMap<>();

    @Override
    public void addBinding(ResourceKey<CreativeModeTab> tab, Supplier<? extends Item> item) {
        bindings.computeIfAbsent(tab, k -> new ArrayList<>()).add(item);
    }

    @Override
    public void registerListeners() {
        IEventBus modBus = IPlatformHelper.INSTANCE.getModEventBus();
        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            List<Supplier<? extends Item>> items = bindings.get(event.getTabKey());
            if (items == null)
                return;
            items.forEach(item -> event.accept(item.get()));
        });
    }
}
