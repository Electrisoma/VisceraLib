package net.electrisoma.visceralib.registry;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.ItemEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class VisceraLibItems {
    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static void init() {
        VisceraLib.LOGGER.info("Registering Items for " + VisceraLib.NAME);
    }

    public static final ItemEntry<Item> TEST_ITEM = REGISTRAR
            .item("test_item", Item::new)
            .properties(p -> p
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
            )
            .lang("Test Item")
            .register();
}
