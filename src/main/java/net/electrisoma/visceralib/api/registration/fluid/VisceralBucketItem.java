package net.electrisoma.visceralib.api.registration.fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class VisceralBucketItem extends BucketItem {
    public VisceralBucketItem(Supplier<? extends FlowingFluid> fluidSupplier, Item.Properties properties) {
        super(fluidSupplier.get(), properties);
    }
}
