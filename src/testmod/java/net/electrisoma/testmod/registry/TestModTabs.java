package net.electrisoma.testmod.registry;

import net.electrisoma.testmod.TestMod;
import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class TestModTabs {
    private static final VisceralRegistrar REGISTRAR = TestMod.registrar();

    public static void init() {
        TestMod.LOGGER.info("Registering Tabs for " + TestMod.NAME);
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
