package net.electrisoma.visceralib.impl.registration.v1.test.helper.builders;

import net.electrisoma.visceralib.api.registration.v1.registry.register.AbstractRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BlockBuilder<T extends Block, H extends AbstractRegistrationHelper<?>> {

    private final H helper;
    private final String name;
    private final Function<Block.Properties, T> factory;
    private Block.Properties properties = Block.Properties.of();
    private boolean shouldRegisterItem = false;
    private final UnaryOperator<Item.Properties> itemConfig = op -> op;

    public BlockBuilder(H helper, String name, Function<Block.Properties, T> factory) {
        this.helper = helper;
        this.name = name;
        this.factory = factory;
    }

    public BlockBuilder<T, H> properties(UnaryOperator<Block.Properties> op) {
        this.properties = op.apply(this.properties);
        return this;
    }

    public BlockBuilder<T, H> withSimpleItem() {
        this.shouldRegisterItem = true;
        return this;
    }

    public RegistryObject<T> register() {
        RegistryObject<T> blockObj = helper.register(BuiltInRegistries.BLOCK, name, () -> factory.apply(properties));

        if (shouldRegisterItem) {
            helper.register(BuiltInRegistries.ITEM, name,
                    () -> new BlockItem(blockObj.get(), itemConfig.apply(new Item.Properties())));
        }

        return blockObj;
    }
}