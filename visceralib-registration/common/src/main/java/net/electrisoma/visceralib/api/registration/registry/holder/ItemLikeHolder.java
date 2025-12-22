package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import org.jetbrains.annotations.NotNull;

public class ItemLikeHolder<T extends ItemLike> extends BaseHolder<T> implements ItemLike {
    protected ItemLikeHolder(HolderOwner<T> owner, ResourceKey<T> key) {
        super(owner, key);
    }

    public ItemStack asStack() {
        return new ItemStack(this);
    }

    public ItemStack asStack(int count) {
        return new ItemStack(this, count);
    }

    public boolean is(ItemStack stack) {
        return is(stack.getItem());
    }

    public boolean is(Item item) {
        return asItem() == item;
    }

    @Override
    public Item asItem() {
        return value().asItem();
    }
}