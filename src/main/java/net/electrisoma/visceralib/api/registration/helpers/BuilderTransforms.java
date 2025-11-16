package net.electrisoma.visceralib.api.registration.helpers;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.builders.BlockBuilder;
import net.electrisoma.visceralib.api.registration.builders.ItemBuilder;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BuilderTransforms {
    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> BuilderTransform<BlockBuilder<T, R>> pickaxeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }
    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> BuilderTransform<BlockBuilder<T, R>> axeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
    }
    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> BuilderTransform<BlockBuilder<T, R>> hoeOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_HOE);
    }
    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> BuilderTransform<BlockBuilder<T, R>> shovelOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_SHOVEL);
    }
    public static <T extends Block, R extends AbstractVisceralRegistrar<R>> BuilderTransform<BlockBuilder<T, R>> axeOrPickaxe() {
        return b -> b
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

//    public static <T extends Item, R extends AbstractVisceralRegistrar<R>> Function<ItemBuilder<T, R>, ItemBuilder<T, R>> CustomRenderedItem(Supplier<? extends ItemRenderer> rendererSupplier) {
//        return builder -> builder.setCustomRendererFactory(() -> rendererSupplier);
//    }
}
