//package net.electrisoma.visceralib.api.registration.builders;
//
//import net.electrisoma.visceralib.api.registration.VisceralRegistry;
//import net.electrisoma.visceralib.api.registration.holders.FluidHolder;
//import net.electrisoma.visceralib.api.registration.holders.ItemHolder;
//import net.minecraft.world.item.BucketItem;
//import net.minecraft.world.level.block.LiquidBlock;
//import net.minecraft.world.level.material.FlowingFluid;
//
//import java.util.Optional;
//import java.util.function.Supplier;
//
//public class FluidBuilder {
//    private final VisceralRegistry registry;
//    private final String name;
//
//    private Supplier<? extends FlowingFluid> sourceSupplier;
//    private Supplier<? extends FlowingFluid> flowingSupplier;
//
//    private FluidHolder<FlowingFluid> sourceHolder;
//    private FluidHolder<FlowingFluid> flowingHolder;
//
//    private boolean registerBucket = false;
//    private boolean registerBlock = false;
//
//    private Supplier<LiquidBlock> blockSupplier;
//    private Supplier<BucketItem> bucketSupplier;
//
//    private FluidHolder<LiquidBlock> blockHolder;
//    private ItemHolder<BucketItem> bucketHolder;
//
//    public FluidBuilder(VisceralRegistry registry, String name) {
//        this.registry = registry;
//        this.name = name;
//    }
//
//    public FluidBuilder source(Supplier<? extends FlowingFluid> source) {
//        this.sourceSupplier = source;
//        return this;
//    }
//
//    public FluidBuilder flowing(Supplier<? extends FlowingFluid> flowing) {
//        this.flowingSupplier = flowing;
//        return this;
//    }
//
//    public FluidBuilder withBlock(Supplier<LiquidBlock> supplier) {
//        this.blockSupplier = supplier;
//        this.registerBlock = true;
//        return this;
//    }
//
//    public FluidBuilder withBucket(Supplier<BucketItem> supplier) {
//        this.bucketSupplier = supplier;
//        this.registerBucket = true;
//        return this;
//    }
//
//    public void register() {
//        // Source fluid
//        sourceHolder = registry
//                .fluid(name, sourceSupplier)
//                .register();
//
//        // Flowing fluid
//        flowingHolder = registry
//                .fluid("flowing_" + name, flowingSupplier)
//                .register();
//
//        if (registerBlock) {
//            blockHolder = registry
//                    .block(name + "_fluid_block", blockSupplier)
//                    .register();
//        }
//
//        if (registerBucket) {
//            bucketHolder = registry
//                    .item(name + "_bucket", bucketSupplier)
//                    .register();
//        }
//    }
//
//    public FluidHolder<FlowingFluid> getSource() {
//        return sourceHolder;
//    }
//
//    public FluidHolder<FlowingFluid> getFlowing() {
//        return flowingHolder;
//    }
//
//    public Optional<FluidHolder<LiquidBlock>> getBlock() {
//        return Optional.ofNullable(blockHolder);
//    }
//
//    public Optional<ItemHolder<BucketItem>> getBucket() {
//        return Optional.ofNullable(bucketHolder);
//    }
//}
//
