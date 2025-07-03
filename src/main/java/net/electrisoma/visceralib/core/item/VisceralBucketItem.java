package net.electrisoma.visceralib.core.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class VisceralBucketItem extends BucketItem {
    private final Supplier<? extends Fluid> fluidSupplier;

    public VisceralBucketItem(Supplier<? extends Fluid> fluidSupplier, Properties properties) {
        super(fluidSupplier.get(), properties);
        this.fluidSupplier = fluidSupplier;
    }

    public Fluid getFluid() {
        return fluidSupplier.get();
    }
}
