package net.electrisoma.visceralib.event.rendering.v1.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public final class ColorHandlerEvents {

    @FunctionalInterface
    public interface ItemColorHandler {

        void register(ItemColor color, ItemLike... items);
    }

    @FunctionalInterface
    public interface BlockColorHandler {

        void register(BlockColor color, Block... blocks);
    }
}
