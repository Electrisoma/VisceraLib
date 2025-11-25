package net.electrisoma.visceralib.registry.builder;

import net.electrisoma.visceralib.registry.VisceralRegistry;
import net.electrisoma.visceralib.registry.holder.BlockHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder<B extends Block> extends AbstractBuilder<Block, B, BlockHolder<B>> {

    private final Function<Properties, B> factory;
    private Supplier<Properties> initialProperties = Properties::of;
    private Function<Properties, Properties> properties = Function.identity();

    public BlockBuilder(
            VisceralRegistry owner,
            String name,
            Function<Properties, B> factory
    ) {
        super(owner, name, BuiltInRegistries.BLOCK);
        this.factory = factory;
    }

    public BlockBuilder<B> initialProperties(Block block) {
        initialProperties = () ->
                /*? =1.21.1 {*/Properties.ofFullCopy
                /*?} else {*//*Properties.copy*//*?}*/
                        (block);
        return this;
    }

    public BlockBuilder<B> initialProperties(Supplier<B> block) {
        initialProperties = () ->
                /*? =1.21.1 {*/Properties.ofFullCopy
                /*?} else {*//*Properties.copy*//*?}*/
                        (block.get());
        return this;
    }

    public BlockBuilder<B> properties(Function<Properties, Properties> properties) {
        this.properties = this.properties.andThen(properties);
        return this;
    }

    @Override
    B build() {
        Properties properties = initialProperties.get();
        properties = this.properties.apply(properties);
        return factory.apply(properties);
    }

    @Override
    BlockHolder<B> getHolder(HolderOwner<Block> owner, ResourceKey<Block> key) {
        //noinspection unchecked
        return (BlockHolder<B>) new BlockHolder<>(owner, key);
    }
}