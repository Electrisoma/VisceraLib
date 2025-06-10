package net.electrisoma.resotech.providers;

import net.electrisoma.resotech.ResoTech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Optional;

public class BlockStateGen extends BlockStateProvider {
    public BlockStateGen(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(ResoTech.MOD_ID))
                .forEach(entry -> {
                    Block block = entry.getValue();

                    if (block.getClass().getSimpleName().contains("LiquidBlock") || block.getClass().getSimpleName().contains("Fluid")) {
                        String blockName = entry.getKey().location().getPath();
                        simpleBlock(block,
                                models().getBuilder(blockName)
                                        .texture("particle", ResoTech.path("fluid/" + blockName + "_still"))
                        );
                    } else {
                        simpleBlock(block);
                    }
                });
    }

    public Optional<VariantBlockStateBuilder> getExistingVariantBuilder(Block block) {
        return Optional.ofNullable(registeredBlocks.get(block))
                .filter(b -> b instanceof VariantBlockStateBuilder)
                .map(b -> (VariantBlockStateBuilder) b);
    }
    public Optional<MultiPartBlockStateBuilder> getExistingMultipartBuilder(Block block) {
        return Optional.ofNullable(registeredBlocks.get(block))
                .filter(b -> b instanceof MultiPartBlockStateBuilder)
                .map(b -> (MultiPartBlockStateBuilder) b);
    }
    public ExistingFileHelper getExistingFileHelper() {
        return models().existingFileHelper;
    }

    @Override
    public String getName() {
        return ResoTech.MOD_ID + " Blockstates";
    }
}
