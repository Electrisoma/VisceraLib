package net.electrisoma.visceralib.registry;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import net.minecraft.world.item.*;

@SuppressWarnings("unused")
public class VisceraLibTabs {
    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static void init() {
        VisceraLib.LOGGER.info("Registering Tabs for " + VisceraLib.NAME);
    }

    public static final TabEntry<CreativeModeTab> BASE = REGISTRAR
            .tab("base")
            .icon(Items.DIRT)
            .lang("Base Tab")
            .register();

    public static final TabEntry<CreativeModeTab> MACHINES = REGISTRAR
            .tab("machines")
            .icon(Items.DIRT)
            .lang("Machines")
            .register();
}
