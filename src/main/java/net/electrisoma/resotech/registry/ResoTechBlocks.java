package net.electrisoma.resotech.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.registry.helpers.BlockBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@SuppressWarnings("unused")
public class ResoTechBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ResoTech.MOD_ID, Registries.BLOCK);

    public static void init() {
        BLOCKS.register();
        ResoTech.LOGGER.info("Registering Blocks for " + ResoTech.NAME);
    }

    public static final RegistrySupplier<Block> MACHINE_BLOCK =
            new BlockBuilder(BLOCKS, "machine_block")
                    .properties(BlockBehaviour.Properties.of()
                            .strength(1.5f))
                    .withBlockItem(ResoTechItems.ITEMS)
                    .lang("Machine Thing")
                    .tab(ResoTechTabs.MACHINES)
                    .register();
}
