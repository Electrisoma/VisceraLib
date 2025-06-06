package net.electrisoma.resotech.fabric.providers;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.BlockStateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.MultiPartBlockStateBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.VariantBlockStateBuilder;
import net.electrisoma.resotech.ResoTech;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;

import net.minecraft.world.level.block.Block;

import java.util.Optional;

@SuppressWarnings("unused")
public class BlockstateGen extends BlockStateProvider {
    public BlockstateGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ResoTech.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BuiltInRegistries.BLOCK
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(ResoTech.MOD_ID))
                .forEach(entry -> {
                    Block block = entry.getValue();
                    simpleBlock(block);
                });
    }

    @Override
    public String getName() {
        return ResoTech.MOD_ID + "Blockstates";
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
}
