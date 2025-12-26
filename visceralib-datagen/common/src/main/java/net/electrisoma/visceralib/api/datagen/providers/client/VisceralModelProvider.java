package net.electrisoma.visceralib.api.datagen.providers.client;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class VisceralModelProvider implements DataProvider {

    protected final PackOutput.PathProvider blockStatePathProvider;
    protected final PackOutput.PathProvider modelPathProvider;
    private final String modid;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public VisceralModelProvider(
            PackOutput output,
            String modid,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        this.modid = modid;
        this.lookupProvider = lookupProvider;
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    protected abstract void registerModels(HolderLookup.Provider lookup, BlockModelGenerators blockGen, ItemModelGenerators itemGen);

    @Override @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput output) {

        return this.lookupProvider.thenCompose(lookup -> {
            Map<Block, BlockStateGenerator> blockStates = Maps.newHashMap();
            Consumer<BlockStateGenerator> blockStateConsumer = (gen) -> {
                if (blockStates.put(gen.getBlock(), gen) != null)
                    throw new IllegalStateException("Duplicate blockstate definition for " + gen.getBlock());
            };

            Map<ResourceLocation, Supplier<JsonElement>> models = Maps.newHashMap();
            BiConsumer<ResourceLocation, Supplier<JsonElement>> modelConsumer = (id, supplier) -> {
                if (models.put(id, supplier) != null)
                    throw new IllegalStateException("Duplicate model definition for " + id);
            };

            registerModels(
                    lookup,
                    new BlockModelGenerators(blockStateConsumer, modelConsumer, (item) -> {}),
                    new ItemModelGenerators(modelConsumer)
            );

            CompletableFuture<?>[] futures = new CompletableFuture[2];
            futures[0] = saveCollection(output, blockStates, (block) ->
                    this.blockStatePathProvider.json(block.builtInRegistryHolder().key().location()));
            futures[1] = saveCollection(output, models, this.modelPathProvider::json);

            return CompletableFuture.allOf(futures);
        });
    }

    private <T> CompletableFuture<?> saveCollection(
            CachedOutput output,
            Map<T, ? extends Supplier<JsonElement>> map,
            Function<T, Path> pathResolver
    ) {
        return CompletableFuture.allOf(map.entrySet().stream()
                .map(entry ->
                        DataProvider.saveStable(output, entry.getValue().get(), pathResolver.apply(entry.getKey()))
                ).toArray(CompletableFuture[]::new)
        );
    }

    @Override @NotNull
    public String getName() {
        return "VisceralModelProvider for " + modid;
    }
}