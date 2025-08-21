package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.api.registration.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.holders.BlockHolder;
import net.electrisoma.visceralib.api.registration.holders.CreativeTabHolder;
import net.electrisoma.visceralib.api.registration.holders.ItemHolder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.material.MapColor;


public class VisceralibReg {
    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static BlockHolder<RedstoneLampBlock> BLOCK = REGISTRY
            .block("test", RedstoneLampBlock::new)
            .properties(p -> p
                    .strength(1.0F, 10000.0F)
                    .mapColor(MapColor.COLOR_GRAY))
            .register();

    public static ItemHolder<BlockItem> TEST_BLOCK_ITEM = REGISTRY
            .item("test", () -> new BlockItem(VisceralibReg.BLOCK.value(), new Item.Properties()))
            .register();

    public static Holder.Reference<CreativeModeTab> CREATIVE_TAB = REGISTRY
            .creativeTab("my_tab", CreativeModeTab.Row.TOP, 1)
            .title(Component.literal("My Tab"))
            .icon(() -> new ItemStack(VisceralibReg.TEST_BLOCK_ITEM.value()))
            .displayItems((params, output) -> {
                output.accept(VisceralibReg.TEST_BLOCK_ITEM.value());
            })
            .register();

    public static void init() {}
}
