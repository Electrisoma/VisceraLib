package net.electrisoma.visceralib.impl.registration;

import net.electrisoma.visceralib.api.registration.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.registry.register.VisceralRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TestRegistry {

    public static final VisceralRegistry REGISTRY = Constants.registry();

    public static final RegistryObject<Item> TEST_ITEM = REGISTRY
            .item("test_item", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Block> TEST_BLOCK = REGISTRY
            .block("test_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));

    public static final RegistryObject<BlockItem> TEST_BLOCK_ITEM = REGISTRY
            .item("test_block", () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties()));

    public static void init() {}
}
