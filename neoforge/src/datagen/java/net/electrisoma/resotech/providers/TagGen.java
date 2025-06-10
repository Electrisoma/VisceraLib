package net.electrisoma.resotech.providers;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.BlockBuilder;
import net.electrisoma.resotech.api.registration.FluidBuilder;
import net.electrisoma.resotech.api.registration.ItemBuilder;
import net.electrisoma.resotech.registry.ResoTechTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TagGen {
    public static class ItemTagGen extends TagsProvider<Item> {
        public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, ExistingFileHelper helper) {
            super(output, Registries.ITEM, lookupProvider, modid, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (ItemBuilder builder : ItemBuilder.getAllBuilders()) {
                var item = builder.register().get();
                var holder = provider.lookupOrThrow(Registries.ITEM).get(item.builtInRegistryHolder().key());

                if (holder.isPresent()) {
                    for (TagKey<Item> tag : builder.getItemTags()) {
                        tag(tag).add(holder.get().key());
                    }
                }
            }

            for (ResoTechTags.AllItemTags tagEnum : ResoTechTags.AllItemTags.values()) {
                if (tagEnum.alwaysDatagen) {
                    tag(tagEnum.tag);
                }
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Item Tags";
        }
    }

    public static class BlockTagGen extends TagsProvider<Block> {
        public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, ExistingFileHelper helper) {
            super(output, Registries.BLOCK, lookupProvider, modid, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
            for (BlockBuilder builder : BlockBuilder.getAllBuilders()) {
                var block = builder.getRegisteredBlock().get();
                var holder = provider.lookupOrThrow(Registries.BLOCK).get(block.builtInRegistryHolder().key());

                if (holder.isPresent()) {
                    for (TagKey<Block> tag : builder.getBlockTags()) {
                        tag(tag).add(holder.get().key());
                    }
                }
            }

            for (ResoTechTags.AllBlockTags tagEnum : ResoTechTags.AllBlockTags.values()) {
                if (tagEnum.alwaysDatagen) {
                    tag(tagEnum.tag);
                }
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Block Tags";
        }
    }

    public static class FluidTagGen extends TagsProvider<Fluid> {
        public FluidTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, ExistingFileHelper helper) {
            super(output, Registries.FLUID, lookupProvider, modid, helper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            var fluidLookup = provider.lookupOrThrow(Registries.FLUID);

            for (FluidBuilder builder : FluidBuilder.getAllBuilders()) {
                var still = builder.getStill().get();
                var flowing = builder.getFlowing().get();

                var stillHolder = fluidLookup.get(still.builtInRegistryHolder().key());
                var flowingHolder = fluidLookup.get(flowing.builtInRegistryHolder().key());

                if (stillHolder.isPresent() && flowingHolder.isPresent()) {
                    for (TagKey<Fluid> tag : builder.getFluidTags()) {
                        tag(tag)
                                .add(stillHolder.get().key())
                                .add(flowingHolder.get().key());
                    }
                }
            }

            for (ResoTechTags.AllFluidTags tagEnum : ResoTechTags.AllFluidTags.values()) {
                if (tagEnum.alwaysDatagen) {
                    tag(tagEnum.tag);
                }
            }
        }

        @Override
        public String getName() {
            return ResoTech.NAME + " Fluid Tags";
        }
    }
}
