package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.holders.BlockHolder;
import net.electrisoma.visceralib.api.registration.holders.ItemHolder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemBuilder<T extends Item> extends AbstractBuilder<Item, T, ItemHolder<T>> {
    private final Supplier<T> itemSupplier;

    public ItemBuilder(VisceralRegistry owner, String name, Supplier<T> itemSupplier) {
        super(owner, name, BuiltInRegistries.ITEM);
        this.itemSupplier = itemSupplier;
    }

    @Override
    public T build() {
        return itemSupplier.get();
    }

    @Override @SuppressWarnings("unchecked")
    public ItemHolder<T> getHolder(HolderOwner<Item> owner, ResourceKey<Item> key) {
        return (ItemHolder<T>) new ItemHolder<>(owner, key);
    }
}
