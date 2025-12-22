package net.electrisoma.visceralib.api.registration.registry.builder;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.ItemHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Function;

public class ItemBuilder<T extends Item> extends AbstractBuilder<Item, T, ItemHolder<T>> {
    private final Function<Properties, T> factory;
    private Function<Properties, Properties> properties = Function.identity();

    public ItemBuilder(VisceralRegistry owner, String name, Function<Properties, T> factory) {
        super(owner, name, BuiltInRegistries.ITEM);
        this.factory = factory;
    }

    public ItemBuilder<T> properties(Function<Properties, Properties> func) {
        properties = properties.andThen(func);
        return this;
    }

    @Override
    T build() {
        Properties properties = new Item.Properties();
        properties = this.properties.apply(properties);
        return factory.apply(properties);
    }

    @Override
    ItemHolder<T> getHolder(HolderOwner<Item> owner, ResourceKey<Item> key) {
        //noinspection unchecked
        return (ItemHolder<T>) new ItemHolder<>(owner, key);
    }
}