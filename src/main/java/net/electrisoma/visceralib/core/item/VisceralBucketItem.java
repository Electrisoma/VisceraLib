package net.electrisoma.visceralib.core.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class VisceralBucketItem extends BucketItem {
    public VisceralBucketItem(Supplier<? extends FlowingFluid> fluidSupplier, Item.Properties properties) {
        super(fluidSupplier.get(), properties);
    }
}
