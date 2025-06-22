package net.electrisoma.visceralib.api.registration.helpers;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

@SuppressWarnings("unused")
public class BuilderTransforms {
    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> Function<BlockBuilder<T, R>, BlockBuilder<T, R>> pickaxeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> Function<BlockBuilder<T, R>, BlockBuilder<T, R>> axeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
    }

    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> Function<BlockBuilder<T, R>, BlockBuilder<T, R>> hoeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_HOE);
    }

    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> Function<BlockBuilder<T, R>, BlockBuilder<T, R>> shovelOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> Function<BlockBuilder<T, R>, BlockBuilder<T, R>> axeOrPickaxe() {
        return b -> b
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }
}
