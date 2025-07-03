package net.electrisoma.visceralib.data.providers.forge;

import net.electrisoma.visceralib.data.providers.VisceralibTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class VisceralibTagProviderImpl {
    public static TagsProvider<Block> createBlockTagsImpl(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, Object fileHelper) {
        return new BlockTagsImpl(output, lookup, modId, (ExistingFileHelper) fileHelper);
    }
    public static TagsProvider<Item> createItemTagsImpl(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, Object fileHelper) {
        return new ItemTagsImpl(output, lookup, modId, (ExistingFileHelper) fileHelper);
    }

    private static class BlockTagsImpl extends VisceralibTagProvider.CommonBlockTags {
        private final ExistingFileHelper fileHelper;

        public BlockTagsImpl(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, ExistingFileHelper helper) {
            super(output, lookup, modId);
            this.fileHelper = helper;
        }
    }
    private static class ItemTagsImpl extends VisceralibTagProvider.CommonItemTags {
        private final ExistingFileHelper fileHelper;

        public ItemTagsImpl(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, ExistingFileHelper helper) {
            super(output, lookup, modId);
            this.fileHelper = helper;
        }
    }
}
