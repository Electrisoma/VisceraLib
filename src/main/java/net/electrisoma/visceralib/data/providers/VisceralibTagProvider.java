package net.electrisoma.visceralib.data.providers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class VisceralibTagProvider {
    public static TagsProvider<Block> createBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, Object fileHelper) {
        return createBlockTagsImpl(output, lookup, modId, fileHelper);
    }
    public static TagsProvider<Item> createItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, Object fileHelper) {
        return createItemTagsImpl(output, lookup, modId, fileHelper);
    }

    @ExpectPlatform public static TagsProvider<Block> createBlockTagsImpl(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, Object fileHelper) {
        throw new AssertionError();
    }
    @ExpectPlatform public static TagsProvider<Item> createItemTagsImpl(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, Object fileHelper) {
        throw new AssertionError();
    }

    public static abstract class CommonBlockTags extends TagsProvider<Block> {
        protected final String modId;

        protected CommonBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId) {
            super(output, Registries.BLOCK, lookup);
            this.modId = modId;
        }

        protected void addBlockTagsLogic(@NotNull HolderLookup.Provider provider) {
            var blockLookup = provider.lookupOrThrow(Registries.BLOCK);
            for (BlockBuilder<?, ?> builder : BlockBuilder.getAllBuilders()) {
                builder.getRegisteredSupplier().ifPresent(supplier -> {
                    Block block = supplier.get();
                    blockLookup.get(block.builtInRegistryHolder().key()).ifPresent(holder -> {
                        for (TagKey<Block> tag : builder.getTags()) {
                            tag(tag).add(holder.key());
                        }
                    });
                });
            }
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            addBlockTagsLogic(provider);
        }

        @Override
        public String getName() {
            return modId + " Block Tags";
        }
    }
    public static abstract class CommonItemTags extends TagsProvider<Item> {
        protected final String modId;

        protected CommonItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId) {
            super(output, Registries.ITEM, lookup);
            this.modId = modId;
        }

        protected void addItemTagsLogic(@NotNull HolderLookup.Provider provider) {
            var itemLookup = provider.lookupOrThrow(Registries.ITEM);
            for (ItemBuilder<?, ?> builder : ItemBuilder.getAllBuilders()) {
                builder.getRegisteredSupplier().ifPresent(supplier -> {
                    Item item = supplier.get();
                    itemLookup.get(item.builtInRegistryHolder().key()).ifPresent(holder -> {
                        for (TagKey<Item> tag : builder.getTags()) {
                            tag(tag).add(holder.key());
                        }
                    });
                });
            }
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            addItemTagsLogic(provider);
        }

        @Override
        public String getName() {
            return modId + " Item Tags";
        }
    }
}
