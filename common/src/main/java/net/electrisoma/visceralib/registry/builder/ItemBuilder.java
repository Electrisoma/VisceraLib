package net.electrisoma.visceralib.registry.builder;

import net.electrisoma.visceralib.registry.VisceralRegistry;
import net.electrisoma.visceralib.registry.holder.ItemHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Function;

public class ItemBuilder<I extends Item> extends AbstractBuilder<Item, I, ItemHolder<I>> {

    private final Function<Properties, I> factory;
    private Function<Properties, Properties> properties = Function.identity();

    public ItemBuilder(
            VisceralRegistry owner,
            String name,
            Function<Properties, I> factory
    ) {
        super(owner, name, BuiltInRegistries.ITEM);
        this.factory = factory;
    }

    public ItemBuilder<I> properties(Function<Properties, Properties> func) {
        properties = properties.andThen(func);
        return this;
    }

    @Override
    I build() {
        Properties properties = new Item.Properties();
        properties = this.properties.apply(properties);
        return factory.apply(properties);
    }

    @Override
    ItemHolder<I> getHolder(HolderOwner<Item> owner, ResourceKey<Item> key) {
        //noinspection unchecked
        return (ItemHolder<I>) new ItemHolder<>(owner, key);
    }
}