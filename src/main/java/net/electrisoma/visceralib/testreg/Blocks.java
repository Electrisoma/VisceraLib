package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.BlockEntry;
import net.electrisoma.visceralib.api.registration.helpers.SharedProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import static net.electrisoma.visceralib.api.registration.helpers.BuilderTransforms.pickaxeOnly;

public class Blocks {
    public static void init() {
        VisceraLib.LOGGER.info("Registering Blocks for " + VisceraLib.NAME);
    }

    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static final BlockEntry<Block> MACHINE_BLOCK = REGISTRAR
            .block("machine_block", Block::new)
            .initialProperties(SharedProperties.netheriteMetal())
            .properties(p -> p
                    .strength(1.0F, 10000.0F)
                    .mapColor(MapColor.COLOR_GRAY))
            .transform(pickaxeOnly())
            .register();
}
