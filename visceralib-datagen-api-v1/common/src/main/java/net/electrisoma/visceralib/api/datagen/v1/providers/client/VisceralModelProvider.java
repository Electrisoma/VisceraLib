package net.electrisoma.visceralib.api.datagen.v1.providers.client;

import net.electrisoma.visceralib.api.datagen.v1.providers.client.model.ModelBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
		this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		this.modid = modid;
		this.lookupProvider = lookupProvider;
	}

	protected abstract void registerModels(HolderLookup.Provider lookup, VisualModelBuilder builder);

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		return this.lookupProvider.thenCompose(lookup -> {

			Map<Block, BlockStateGenerator> blockStates = Maps.newHashMap();
			Map<ResourceLocation, Supplier<JsonElement>> models = Maps.newHashMap();

			VisualModelBuilder builder = new VisualModelBuilder() {
				@Override
				public void addModel(ResourceLocation id, Supplier<JsonElement> supplier) {
					if (models.put(id, supplier) != null)
						throw new IllegalStateException("Duplicate model: " + id);
				}

				@Override
				public void addBlockState(BlockStateGenerator gen) {
					if (blockStates.put(gen.getBlock(), gen) != null)
						throw new IllegalStateException("Duplicate blockstate: " + gen.getBlock());
				}
			};

			registerModels(lookup, builder);

			CompletableFuture<?>[] futures = new CompletableFuture[2];
			futures[0] = saveCollection(output, blockStates, (block) ->
					this.blockStatePathProvider.json(BuiltInRegistries.BLOCK.getKey(block)));
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
						DataProvider.saveStable(
								output,
								entry.getValue().get(),
								pathResolver.apply(entry.getKey())
						))
				.toArray(CompletableFuture[]::new)
		);
	}

	@Override
	public String getName() {
		return "VisceralModelProvider: " + modid;
	}

	public interface VisualModelBuilder {

		void addModel(ResourceLocation id, Supplier<JsonElement> supplier);
		void addBlockState(BlockStateGenerator gen);

		default void item(Item item, ModelTemplate template) {
			template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this::addModel);
		}

		default void item(Item item, ModelTemplate template, TextureMapping mapping) {
			template.create(ModelLocationUtils.getModelLocation(item), mapping, this::addModel);
		}

		default void parentedItem(Item item, Block block) {
			ModelTemplates.FLAT_ITEM.create(
					ModelLocationUtils.getModelLocation(item),
					new TextureMapping().put(TextureSlot.PARTICLE, ModelLocationUtils.getModelLocation(block)),
					this::addModel
			);
		}

		default void block(Block block, ModelTemplate template) {
			ResourceLocation modelLoc = template.create(block, TextureMapping.cube(block), this::addModel);
			addBlockState(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modelLoc)));
		}

		default void blockWithItem(Block block, ModelTemplate template) {
			ResourceLocation modelLoc = template.create(block, TextureMapping.cube(block), this::addModel);
			addBlockState(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modelLoc)));

			ModelTemplates.FLAT_ITEM.create(
					ModelLocationUtils.getModelLocation(block.asItem()),
					new TextureMapping().put(TextureSlot.PARTICLE, modelLoc),
					this::addModel
			);
		}

		default ModelBuilder build(ModelTemplate template) {
			return new ModelBuilder(template, this);
		}
	}
}
