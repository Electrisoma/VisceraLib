package net.electrisoma.resotech.registry;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.BlockBuilder;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

import static net.electrisoma.resotech.api.registration.BlockTransforms.*;

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
                    .properties(p -> p
                            .strength(1.0F, 10000.0F)
                            .mapColor(MapColor.COLOR_GRAY))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .withBlockItem(ResoTechItems.ITEMS)
                    .lang("Machine Thing")
                    .tab(ResoTechTabs.MACHINES)
                    .register();

//    public static final RegistrySupplier<LiquidBlock> TEST_FLUID =
//            BLOCKS.register("test", () ->
//                    new ArchitecturyLiquidBlock(ResoTechFluids.TEST_STILL,
//                            BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
}
