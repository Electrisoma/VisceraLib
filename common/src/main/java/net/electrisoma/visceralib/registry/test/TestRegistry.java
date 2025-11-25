package net.electrisoma.visceralib.registry.test;

import net.electrisoma.visceralib.registry.VisceralRegistry;
import net.electrisoma.visceralib.registry.holder.RegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;

public class TestRegistry {

    public static VisceralRegistry REGISTRY = new VisceralRegistry("visceralib");

    public static RegistryObject<TestBlock> BLOCK = REGISTRY
            .block("test", TestBlock::new)
            .initialProperties(Blocks.DIAMOND_BLOCK)
            .properties(p -> p.strength(1.0F, 10000.0F))
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .register();

    public static final RegistryObject<TestBlock.TestBlockItem> BLOCK_ITEM = REGISTRY
            .item("test", TestBlock.TestBlockItem::new)
            .register();

    public static final RegistryObject<CreativeModeTab> TEST_TAB = REGISTRY
            .tab("test_tab")
            .icon(() -> new ItemStack(BLOCK.get()))
            .add(BLOCK.get())
            .add(Blocks.DIAMOND_BLOCK)
            .register();

    public static void init() {}
}