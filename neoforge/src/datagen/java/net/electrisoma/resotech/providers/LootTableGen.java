package net.electrisoma.resotech.providers;

import net.electrisoma.resotech.api.registration.BlockBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LootTableGen extends LootTableProvider {
    public LootTableGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(
                output,
                Set.of(),
                List.of(new SubProviderEntry(
                        BlockLootTables::new,
                        LootContextParamSets.BLOCK
                )),
                registriesFuture
        );
    }

    private static class BlockLootTables extends BlockLootSubProvider {
        public BlockLootTables(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            for (BlockBuilder builder : BlockBuilder.getAllBuilders()) {
                Block block = builder.getRegisteredBlock().get();

                if (builder.getLootTableProvider().isPresent()) {
                    LootTable.Builder lootTableBuilder = LootTable.lootTable();
                    builder.getLootTableProvider().get().accept(lootTableBuilder, block);
                    this.add(block, lootTableBuilder);

                } else if (builder.getLootDrop().isPresent()) {
                    BlockBuilder.LootDrop drop = builder.getLootDrop().get();
                    this.add(block, createLootTableFromDrop(drop));

                } else {
                    this.dropSelf(block);
                }
            }
        }

        private LootTable.Builder createLootTableFromDrop(BlockBuilder.LootDrop drop) {
            return LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(drop.item())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                            drop.minCount(), drop.maxCount())))));
        }
    }
}
