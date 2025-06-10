package net.electrisoma.resotech.fabric.providers;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.BlockBuilder;
import net.electrisoma.resotech.api.registration.FluidBuilder;
import net.electrisoma.resotech.api.registration.ItemBuilder;
import net.electrisoma.resotech.registry.ResoTechTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class TagGen {
    public static void addGenerators(FabricDataGenerator.Pack pack) {
        pack.addProvider(BlockTags::new);
        pack.addProvider(ItemTags::new);
        pack.addProvider(FluidTags::new);
    }

    public static class ItemTags extends FabricTagProvider<Item> {
        public ItemTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.ITEM, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            var itemLookup = provider.lookupOrThrow(Registries.ITEM);
            for (ItemBuilder builder : ItemBuilder.getAllBuilders()) {
                Item item = builder.register().get();
                var itemKey = item.builtInRegistryHolder().key();

                for (TagKey<Item> tag : builder.getItemTags()) {
                    itemLookup.get(itemKey).ifPresent(holder -> tag(tag).add(holder.key()));
                }
            }

            for (ResoTechTags.AllItemTags tagEnum : ResoTechTags.AllItemTags.values()) {
                if (!tagEnum.alwaysDatagen) continue;
                tag(tagEnum.tag);
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Item Tags";
        }
    }

    public static class BlockTags extends FabricTagProvider<Block> {
        public BlockTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.BLOCK, registriesFuture);
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

            for (ResoTechTags.AllBlockTags tagEnum : ResoTechTags.AllBlockTags.values()) {
                if (!tagEnum.alwaysDatagen) continue;
                tag(tagEnum.tag);
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Block Tags";
        }
    }

    public static class FluidTags extends FabricTagProvider<net.minecraft.world.level.material.Fluid> {
        public FluidTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.FLUID, registriesFuture);
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
                            for (TagKey<net.minecraft.world.level.material.Fluid> tag : builder.getFluidTags()) {
                                tag(tag).add(stillHolder.key()).add(flowingHolder.key());
                            }
                        }));
            }

            for (ResoTechTags.AllFluidTags tagEnum : ResoTechTags.AllFluidTags.values()) {
                if (!tagEnum.alwaysDatagen) continue;
                tag(tagEnum.tag);
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Fluid Tags";
        }
    }
}
