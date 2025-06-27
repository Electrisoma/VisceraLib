package net.electrisoma.testmod.registry;

import net.electrisoma.testmod.TestMod;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.BlockEntry;
import net.electrisoma.visceralib.api.registration.helpers.SharedProperties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import static net.electrisoma.visceralib.api.registration.helpers.BuilderTransforms.pickaxeOnly;

@SuppressWarnings("unused")
public class TestModBlocks {
    private static final VisceralRegistrar REGISTRAR = TestMod.registrar();

    public static void init() {
        TestMod.LOGGER.info("Registering Blocks for " + TestMod.NAME);
    }

    public static final BlockEntry<Block> MACHINE_BLOCK = REGISTRAR
            .block("machine_block", Block::new)
            .initialProperties(SharedProperties.netheriteMetal())
            .properties(p -> p
                    .strength(1.0F, 10000.0F)
                    .mapColor(MapColor.COLOR_GRAY)
            )
            .transform(pickaxeOnly())
            .lang("Machine Thing")
            .register();
}
