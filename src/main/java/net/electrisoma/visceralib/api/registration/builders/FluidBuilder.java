package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.FluidEntry;
import net.electrisoma.visceralib.api.registration.fluid.VisceralFlowingFluid;
import net.electrisoma.visceralib.api.registration.fluid.VisceralFluidAttributes;
import net.electrisoma.visceralib.api.registration.fluid.VisceralLiquidBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
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
    private final Set<CreativeModeTab> creativeTabs = new HashSet<>();
    private final List<TagKey<Fluid>> fluidTags = new ArrayList<>();

    private VisceralRegistrySupplier<VisceralFlowingFluid.Source> stillFluidSupplier;
    private VisceralRegistrySupplier<VisceralFlowingFluid.Flowing> flowingFluidSupplier;
    private VisceralRegistrySupplier<Block> liquidBlockSupplier;
    private VisceralRegistrySupplier<Item> bucketItemSupplier;

    private ResourceLocation stillTexture;
    private ResourceLocation flowTexture;
    private ResourceLocation overlayTexture;
    private int color = 0xFFFFFFFF;
    private int luminance = 0;
    private int density = 1000;
    private int viscosity = 1000;
    private int temperature = 300;

    private Consumer<Item.Properties> itemProperties = p -> {};
    private Consumer<BlockBehaviour.Properties> blockProperties = p -> {};

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

    public FluidBuilder<R> itemProperties(Consumer<Item.Properties> props) {
        this.itemProperties = props;
        return this;
    }

    public FluidBuilder<R> blockProperties(Consumer<BlockBehaviour.Properties> props) {
        this.blockProperties = props;
        return this;
    }

    public FluidBuilder<R> tab(CreativeModeTab tab) {
        creativeTabs.add(tab);
        return this;
    }

    public Set<CreativeModeTab> getTabs() {
        return Collections.unmodifiableSet(creativeTabs);
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

    public FluidEntry<VisceralFlowingFluid.Flowing> register() {
        if (stillTexture == null) stillTexture = path("fluid/" + name + "_still");
        if (flowTexture == null) flowTexture = path("fluid/" + name + "_flow");
        if (overlayTexture == null) overlayTexture = path("gui/" + name + "_overlay");

        final Supplier<VisceralFlowingFluid>[] stillRef = new Supplier[1];
        final Supplier<VisceralFlowingFluid>[] flowingRef = new Supplier[1];

        VisceralFluidAttributes attributes = VisceralFluidAttributes.builder(() -> stillRef[0].get(), () -> flowingRef[0].get())
                .textures(stillTexture, flowTexture)
                .color(color)
                .luminosity(luminance)
                .density(density)
                .viscosity(viscosity)
                .temperature(temperature)
                .build();

        VisceralDeferredRegister<Fluid> fluidRegister = registrar.deferredRegister(Registries.FLUID);

        //noinspection unchecked
        stillFluidSupplier = new VisceralRegistrySupplier<>(
                (ResourceKey<VisceralFlowingFluid.Source>)(ResourceKey<?>) ResourceKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(registrar.getModId(), name)),
                () -> new VisceralFlowingFluid.Source(attributes)
        );
        //noinspection unchecked
        flowingFluidSupplier = new VisceralRegistrySupplier<>(
                (ResourceKey<VisceralFlowingFluid.Flowing>)(ResourceKey<?>) ResourceKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(registrar.getModId(), "flowing_" + name)),
                () -> new VisceralFlowingFluid.Flowing(attributes)
        );

        fluidRegister.register(name, stillFluidSupplier::get);
        fluidRegister.register("flowing_" + name, flowingFluidSupplier::get);

        stillRef[0] = () -> stillFluidSupplier.get();
        flowingRef[0] = () -> flowingFluidSupplier.get();

        Supplier<FlowingFluid> flowingSupplier = flowingFluidSupplier::get;
        Supplier<FlowingFluid> stillSupplier = stillFluidSupplier::get;

        VisceralDeferredRegister<Block> blockRegister = registrar.deferredRegister(Registries.BLOCK);
        liquidBlockSupplier = blockRegister.register(name, () -> {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.WATER);
            blockProperties.accept(props);
            // We create a VisceralLiquidBlock, which is a subclass of LiquidBlock, which is a subclass of Block
            return new VisceralLiquidBlock(stillSupplier, props);
        });

        VisceralDeferredRegister<Item> itemRegister = registrar.deferredRegister(Registries.ITEM);
        bucketItemSupplier = itemRegister.register(name + "_bucket", () -> {
            Item.Properties props = new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1);
            itemProperties.accept(props);
            return new BucketItem(stillSupplier.get(), props);
        });

        return new FluidEntry<>(flowingFluidSupplier);
    }

    public List<TagKey<Fluid>> getFluidTags() {
        return Collections.unmodifiableList(fluidTags);
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langEntry);
    }

    public Supplier<FlowingFluid> getStill() {
        return stillFluidSupplier::get;
    }

    public Supplier<FlowingFluid> getFlowing() {
        return flowingFluidSupplier::get;
    }

    public Supplier<LiquidBlock> getBlock() {
        return () -> (LiquidBlock) liquidBlockSupplier.get();
    }

    public Supplier<Item> getBucket() {
        return bucketItemSupplier::get;
    }
}
