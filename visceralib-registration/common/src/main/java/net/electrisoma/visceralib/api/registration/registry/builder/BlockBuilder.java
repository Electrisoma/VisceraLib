package net.electrisoma.visceralib.api.registration.registry.builder;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.BlockHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder<T extends Block> extends AbstractBuilder<Block, T, BlockHolder<T>> {
    private final Function<Properties, T> factory;
    private Supplier<Properties> initialProperties = Properties::of;
    private Function<Properties, Properties> properties = Function.identity();

    public BlockBuilder(VisceralRegistry owner, String name, Function<Properties, T> factory) {
        super(owner, name, BuiltInRegistries.BLOCK);
        this.factory = factory;
    }

    public BlockBuilder<T> initialProperties(Supplier<T> block) {
        initialProperties = () -> Properties.ofFullCopy(block.get());
        return this;
    }

    public BlockBuilder<T> properties(Function<Properties, Properties> properties) {
        this.properties = this.properties.andThen(properties);
        return this;
    }

    @Override
    T build() {
        Properties properties = initialProperties.get();
        properties = this.properties.apply(properties);
        return factory.apply(properties);
    }

    @Override
    BlockHolder<T> getHolder(HolderOwner<Block> owner, ResourceKey<Block> key) {
        //noinspection unchecked
        return (BlockHolder<T>) new BlockHolder<>(owner, key);
    }
}