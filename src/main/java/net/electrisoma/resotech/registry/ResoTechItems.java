package net.electrisoma.resotech.registry;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.ItemBuilder;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;

public class ResoTechItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ResoTech.MOD_ID, Registries.ITEM);

    public static void init() {
        ITEMS.register();
        ResoTech.LOGGER.info("Registering Items for " + ResoTech.NAME);
    }

    public static final RegistrySupplier<Item> TEST_ITEM =
            new ItemBuilder(ITEMS, "test_item")
                    .lang("Item Thing")
                    .tab(ResoTechTabs.BASE)
                    .register();
}
