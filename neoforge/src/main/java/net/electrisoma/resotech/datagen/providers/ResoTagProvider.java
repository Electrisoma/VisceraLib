package net.electrisoma.resotech.datagen.providers;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.builders.BlockBuilder;
import net.electrisoma.resotech.api.registration.builders.FluidBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;


import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class ResoTagProvider {
    public static class BlockTags extends TagsProvider<Block> {
        public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
            super(output, Registries.BLOCK, lookupProvider, ResoTech.MOD_ID, fileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            var blockLookup = provider.lookupOrThrow(Registries.BLOCK);

            for (BlockBuilder builder : BlockBuilder.getAllBuilders()) {
                Block block = builder.getRegisteredBlock().get();

                blockLookup.get(block.builtInRegistryHolder().key()).ifPresent(holder -> {
                    for (TagKey<Block> tag : builder.getBlockTags()) {
                        tag(tag).add(holder.key());
                    }
                });
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Block Tags";
        }
    }
    public static class ItemTags extends TagsProvider<Item> {
        public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
            super(output, Registries.ITEM, lookupProvider, ResoTech.MOD_ID, fileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Item Tags";
        }
    }
    public static class FluidTags extends TagsProvider<Fluid> {
        public FluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
            super(output, Registries.FLUID, lookupProvider, ResoTech.MOD_ID, fileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            var fluidLookup = provider.lookupOrThrow(Registries.FLUID);

            for (FluidBuilder builder : FluidBuilder.getAllBuilders()) {
                var still = builder.getStill().get();
                var flowing = builder.getFlowing().get();

                var stillKey = still.builtInRegistryHolder().key();
                var flowingKey = flowing.builtInRegistryHolder().key();

                fluidLookup.get(stillKey).ifPresent(stillHolder ->
                        fluidLookup.get(flowingKey).ifPresent(flowingHolder -> {
                            for (TagKey<Fluid> tag : builder.getFluidTags()) {
                                tag(tag).add(stillHolder.key()).add(flowingHolder.key());
                            }
                        }));
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Fluid Tags";
        }
    }
}