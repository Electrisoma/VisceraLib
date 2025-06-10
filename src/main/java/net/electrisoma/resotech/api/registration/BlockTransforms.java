package net.electrisoma.resotech.api.registration;

import net.minecraft.tags.BlockTags;

import java.util.function.Function;

@SuppressWarnings("unused")
public class BlockTransforms {
    public static Function<BlockBuilder, BlockBuilder> pickaxeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }
    public static Function<BlockBuilder, BlockBuilder> axeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
    }
    public static Function<BlockBuilder, BlockBuilder> hoeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_HOE);
    }
    public static Function<BlockBuilder, BlockBuilder> shovelOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_SHOVEL);
    }
    public static Function<BlockBuilder, BlockBuilder> axeOrPickaxe() {
        return b -> b
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }
}
