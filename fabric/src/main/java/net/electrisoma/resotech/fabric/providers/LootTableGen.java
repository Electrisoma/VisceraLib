package net.electrisoma.resotech.fabric.providers;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.BlockBuilder;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;

public class LootTableGen extends FabricBlockLootTableProvider  {
    public LootTableGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
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

    @Override
    public String getName() {
        return ResoTech.NAME + " Block Loot Tables";
    }
}