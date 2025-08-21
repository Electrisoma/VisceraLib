package net.electrisoma.visceralib.data.providers;

import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class VisceralLootProvider extends LootTableProvider {
    public VisceralLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String modid) {
        super(output, Set.of(),/*? =1.21.1 {*/
                /*List.of(new SubProviderEntry(registries ->
                        new BlockLootTables(registries, modid), LootContextParamSets.BLOCK)),
                registriesFuture
                *//*?} else {*/List.of(new SubProviderEntry(() -> {
                    HolderLookup.Provider registries = registriesFuture.join();
                    return new BlockLootTables(registries, modid);
                }, LootContextParamSets.BLOCK))/*?}*/);
    }

    private static class BlockLootTables extends BlockLootSubProvider {
        private final Set<Block> knownBlocks;
        private final String modid;

        public BlockLootTables(HolderLookup.Provider registries, String modid) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), /*? =1.21.1 {*/
                    /*registries*//*?} else {*/null/*?}*/);
            
            this.modid = modid;
            this.knownBlocks = BlockBuilder.getAllBuilders().stream()
                    .map(BlockBuilder::getRegisteredSupplier)
                    .flatMap(Optional::stream)
                    .map(VisceralRegistrySupplier::get)
                    .collect(Collectors.toSet());
        }

        @Override
        public void generate() {
            for (BlockBuilder<?, ?> builder : BlockBuilder.getAllBuilders()) {
                builder.getRegisteredSupplier().ifPresent(supplier -> {
                    Block block = supplier.get();

                    if (builder.getLootTableProvider().isPresent()) {
                        LootTable.Builder lootTableBuilder = LootTable.lootTable();
                        builder.getLootTableProvider().get().accept(lootTableBuilder, block);
                        this.add(block, lootTableBuilder);
                    } else if (builder.getLootDrop().isPresent()) {
                        BlockBuilder.LootDrop drop = builder.getLootDrop().get();
                        this.add(block, createLootTableFromDrop(drop));
                    } else this.dropSelf(block);
                });
            }
        }

        protected Iterable<Block> getKnownBlocks() {
            return knownBlocks;
        }

        private LootTable.Builder createLootTableFromDrop(BlockBuilder.LootDrop drop) {
            return LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(drop.item())
                                    .apply(SetItemCountFunction.setCount(
                                            UniformGenerator.between(drop.minCount(), drop.maxCount())
                                    ))));
        }
    }
}
