package net.electrisoma.resotech.datagen.providers;

import net.electrisoma.resotech.ResoTech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Optional;

public class ResoBlockstateProvider extends BlockStateProvider {
    public ResoBlockstateProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, ResoTech.MOD_ID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(ResoTech.MOD_ID))
                .forEach(entry -> {
                    Block block = entry.getValue();
                    String blockName = entry.getKey().location().getPath();

                    if (block.getClass().getSimpleName().contains("LiquidBlock") ||
                            block.getClass().getSimpleName().contains("Fluid")) {

                        BlockModelBuilder model = models().getBuilder(blockName)
                                .texture("particle", modLoc("fluid/" + blockName + "_still"));

                        simpleBlock(block, model);
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
        return ResoTech.NAME + " Blockstates";
    }
}
