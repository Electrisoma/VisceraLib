package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.api.registration.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.holders.BlockHolder;
import net.electrisoma.visceralib.api.registration.holders.ItemHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.RedstoneLampBlock;


public class VisceralibBlocks {
    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static BlockHolder<RedstoneLampBlock> BLOCK = REGISTRY
            .block("test", RedstoneLampBlock::new)
            .properties()
            .register();

    public static ItemHolder<BlockItem> TEST_BLOCK_ITEM = REGISTRY
            .item("test", () -> new BlockItem(VisceralibBlocks.BLOCK.value(), new Item.Properties()))
            .register();

    public static void init() {}
}
