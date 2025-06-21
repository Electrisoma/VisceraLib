package net.electrisoma.resotech.registry;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.builders.BlockBuilder;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.electrisoma.resotech.api.registration.SharedProperties;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static net.electrisoma.resotech.api.registration.BuilderTransforms.*;

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
                    .initialProperties(SharedProperties::netheriteMetal)
                    .properties(p -> p
                            .strength(1.0F, 10000.0F)
                            .mapColor(MapColor.COLOR_GRAY)
                    )
                    .renderLayer(RenderType.translucent())
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .withBlockItem(ResoTechItems.ITEMS)
                    .lang("Machine Thing")
                    .tab(ResoTechTabs.MACHINES)
                    .register();
}
