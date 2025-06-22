package net.electrisoma.visceralib.registry;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import net.minecraft.world.item.*;

@SuppressWarnings("unused")
public class VisceraLibTabs {
    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static TabEntry<CreativeModeTab> BASE;

    public static void init() {
        BASE = VisceraLib.registrar()
                .tab("base")
                .icon(() -> new ItemStack(VisceraLibItems.TEST_ITEM.get()))
                .lang("Base Tab")
                .register();
        VisceraLib.LOGGER.info("Registered tab: {}", BASE.getKey());
    }
}
