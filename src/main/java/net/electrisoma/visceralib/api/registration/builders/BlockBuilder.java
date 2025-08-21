package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.Registration;
import net.electrisoma.visceralib.api.registration.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.holders.BlockHolder;
import net.electrisoma.visceralib.api.registration.holders.ItemHolder;
import net.electrisoma.visceralib.multiloader.PlatformInfo;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder<T extends Block> extends AbstractBuilder<Block, T, BlockHolder<T>> {
    private final Function<Properties, T> blockFunc;
    private Supplier<Properties> initialProperties = Properties::of;
    private Function<Properties, Properties> properties = Function.identity();

    public BlockBuilder(VisceralRegistry owner, String name, Function<Properties, T> blockFunc) {
        super(owner, name, BuiltInRegistries.BLOCK);
        this.blockFunc = blockFunc;
    }

    public BlockBuilder<T> initialProperties(Supplier<T> block) {
        initialProperties = () -> Properties.ofFullCopy(block.get());
        return this;
    }

    public BlockBuilder<T> properties(Function<Properties, Properties> func) {
        properties = properties.andThen(func);
        return this;
    }

    @Override
    T build() {
        Properties properties = initialProperties.get();
        properties = this.properties.apply(properties);
        return blockFunc.apply(properties);
    }

    @Override @SuppressWarnings("unchecked")
    BlockHolder<T> getHolder(HolderOwner<Block> owner, ResourceKey<Block> key) {
        return (BlockHolder<T>) new BlockHolder<>(owner, key);
    }
}