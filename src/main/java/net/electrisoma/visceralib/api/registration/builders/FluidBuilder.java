package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.FluidEntry;
import net.electrisoma.visceralib.core.block.VisceralLiquidBlock;
import net.electrisoma.visceralib.core.fluid.VisceralFlowingFluid;
import net.electrisoma.visceralib.core.fluid.VisceralFluidAttributes;
import net.electrisoma.visceralib.core.item.VisceralBucketItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.electrisoma.visceralib.VisceraLib.path;

public class FluidBuilder<R extends AbstractVisceralRegistrar<R>> {
    private final R registrar;
    private final String name;

    private String langEntry = null;

    private final Set<Supplier<CreativeModeTab>> creativeTabs = new HashSet<>();
    private final List<TagKey<Fluid>> fluidTags = new ArrayList<>();

    private VisceralRegistrySupplier<VisceralFlowingFluid.Source> stillFluidSupplier;
    private VisceralRegistrySupplier<VisceralFlowingFluid.Flowing> flowingFluidSupplier;

    private BlockBuilder<VisceralLiquidBlock, R> blockBuilder;
    private ItemBuilder<VisceralBucketItem, R> bucketBuilder;
    private Supplier<Item> bucketItemSupplier = () -> null;

    private ResourceLocation stillTexture;
    private ResourceLocation flowTexture;
    private ResourceLocation overlayTexture;

    private int color = 0xFFFFFFFF;
    private int luminance = 0;
    private int density = 1000;
    private int viscosity = 1000;
    private int temperature = 300;

    private Consumer<Item.Properties> itemProperties = props -> {};
    private Consumer<BlockBehaviour.Properties> blockProperties = props -> {};

    private boolean registerBlock = false;
    private boolean registerBucket = false;

    public FluidBuilder(R registrar, String name) {
        this.registrar = registrar;
        this.name = name;
    }

    public FluidBuilder<R> textures(ResourceLocation still, ResourceLocation flowing) {
        this.stillTexture = still;
        this.flowTexture = flowing;
        return this;
    }

    public FluidBuilder<R> color(int color) {
        this.color = color;
        return this;
    }

    public FluidBuilder<R> luminance(int luminance) {
        this.luminance = luminance;
        return this;
    }

    public FluidBuilder<R> density(int density) {
        this.density = density;
        return this;
    }

    public FluidBuilder<R> viscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public FluidBuilder<R> temperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public FluidBuilder<R> itemProperties(Consumer<Item.Properties> consumer) {
        this.itemProperties = consumer;
        return this;
    }

    public FluidBuilder<R> blockProperties(Consumer<BlockBehaviour.Properties> consumer) {
        this.blockProperties = consumer;
        return this;
    }

    public FluidBuilder<R> tab(Supplier<CreativeModeTab> tabSupplier) {
        if (tabSupplier == null) throw new IllegalArgumentException("tabSupplier cannot be null");
        this.creativeTabs.add(tabSupplier);
        return this;
    }

    public FluidBuilder<R> tab(CreativeModeTab tab) {
        this.creativeTabs.add(() -> tab);
        return this;
    }

    public FluidBuilder<R> tag(TagKey<Fluid> tag) {
        this.fluidTags.add(tag);
        return this;
    }

    @SafeVarargs
    public final FluidBuilder<R> tags(TagKey<Fluid>... tags) {
        Collections.addAll(this.fluidTags, tags);
        return this;
    }

    public FluidBuilder<R> lang(String langEntry) {
        this.langEntry = langEntry;
        return this;
    }

    public FluidBuilder<R> withBlock(Consumer<BlockBuilder<VisceralLiquidBlock, R>> blockSetup) {
        this.registerBlock = true;
        this.blockBuilder = new BlockBuilder<>(registrar, name, props -> new VisceralLiquidBlock(() -> stillFluidSupplier.get(), props));
        blockSetup.accept(this.blockBuilder);
        return this;
    }

    public FluidBuilder<R> withBucket(Consumer<ItemBuilder<VisceralBucketItem, R>> bucketSetup) {
        this.registerBucket = true;
        this.bucketBuilder = new ItemBuilder<>(registrar, name + "_bucket", props -> new VisceralBucketItem(() -> stillFluidSupplier.get(), props));
        bucketSetup.accept(this.bucketBuilder);
        return this;
    }

    public List<TagKey<Fluid>> getFluidTags() {
        return Collections.unmodifiableList(fluidTags);
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langEntry);
    }

    public Set<Supplier<CreativeModeTab>> getTabs() {
        return Collections.unmodifiableSet(creativeTabs);
    }

    public FluidEntry<VisceralFlowingFluid.Flowing> register() {
        if (stillTexture == null) stillTexture = path("fluid/" + name + "_still");
        if (flowTexture == null) flowTexture = path("fluid/" + name + "_flow");
        if (overlayTexture == null) overlayTexture = path("gui/" + name + "_overlay");

        VisceralDeferredRegister<Fluid> fluidRegister = registrar.deferredRegister(Registries.FLUID);

        ResourceLocation stillLoc = ResourceLocation.fromNamespaceAndPath(registrar.getModId(), name);
        ResourceLocation flowingLoc = ResourceLocation.fromNamespaceAndPath(registrar.getModId(), "flowing_" + name);

        ResourceKey<VisceralFlowingFluid.Source> stillKey =
                (ResourceKey<VisceralFlowingFluid.Source>) (ResourceKey<?>) ResourceKey.create(Registries.FLUID, stillLoc);
        ResourceKey<VisceralFlowingFluid.Flowing> flowingKey =
                (ResourceKey<VisceralFlowingFluid.Flowing>) (ResourceKey<?>) ResourceKey.create(Registries.FLUID, flowingLoc);

        if (registerBucket && bucketBuilder != null) {
            bucketBuilder.register();

            bucketItemSupplier = () -> bucketBuilder.getRegisteredSupplier()
                    .map(VisceralRegistrySupplier::get)
                    .orElse(null);
        }

        final VisceralFluidAttributes[] attributesHolder = new VisceralFluidAttributes[1];

        stillFluidSupplier = new VisceralRegistrySupplier<>(stillKey, () -> new VisceralFlowingFluid.Source(
                attributesHolder[0],
                () -> flowingFluidSupplier.get(),
                () -> stillFluidSupplier.get(),
                bucketItemSupplier
        ));

        flowingFluidSupplier = new VisceralRegistrySupplier<>(flowingKey, () -> new VisceralFlowingFluid.Flowing(
                attributesHolder[0],
                () -> flowingFluidSupplier.get(),
                () -> stillFluidSupplier.get(),
                bucketItemSupplier
        ));

        attributesHolder[0] = VisceralFluidAttributes.builder(
                        () -> stillFluidSupplier.get(),
                        () -> flowingFluidSupplier.get()
                )
                .textures(stillTexture, flowTexture)
                .color(color)
                .luminosity(luminance)
                .density(density)
                .viscosity(viscosity)
                .temperature(temperature)
                .build();

        fluidRegister.register(name, stillFluidSupplier::get);
        fluidRegister.register("flowing_" + name, flowingFluidSupplier::get);

        if (registerBlock && blockBuilder != null) {
            blockBuilder.blockSupplier(() -> new VisceralLiquidBlock(() -> stillFluidSupplier.get(), blockBuilder.getProperties()));
            blockBuilder.register();
        }

        return new FluidEntry<>(flowingFluidSupplier);
    }

    public Supplier<FlowingFluid> getStill() {
        return stillFluidSupplier::get;
    }

    public Supplier<FlowingFluid> getFlowing() {
        return flowingFluidSupplier::get;
    }

    public Optional<Supplier<Block>> getBlock() {
        if (blockBuilder == null) return Optional.empty();
        return blockBuilder.getRegisteredSupplier()
                .map(supplier -> (Supplier<Block>) supplier);
    }

    public Optional<Supplier<Item>> getBucket() {
        if (bucketBuilder == null) return Optional.empty();
        return bucketBuilder.getRegisteredSupplier()
                .map(supplier -> (Supplier<Item>) supplier);
    }
}
