package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class VisceralibTabs {
    public static void init() {
        VisceraLib.LOGGER.info("Registering VisceralibTabs for " + VisceraLib.NAME);
    }

    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static final TabEntry<CreativeModeTab> BASE = REGISTRAR
            .tab(VisceraLib.MOD_ID)
            .lang(VisceraLib.NAME)
            .icon(() -> new ItemStack(VisceralibBlocks.MACHINE_BLOCK.get()))
            .register();
}
