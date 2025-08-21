package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.FluidEntry;
import net.electrisoma.visceralib.api.registration.helpers.CreativeTabBuilderRegistry;
import net.electrisoma.visceralib.api.registration.helpers.ICreativeTabOutputs;
import net.electrisoma.visceralib.api.registration.helpers.TaggableBuilder;
import net.electrisoma.visceralib.core.block.VisceralLiquidBlock;
import net.electrisoma.visceralib.core.fluid.VisceralFlowingFluid;
import net.electrisoma.visceralib.core.fluid.VisceralFluidAttributes;
import net.electrisoma.visceralib.core.item.VisceralBucketItem;

import net.electrisoma.visceralib.data.providers.VisceralLangProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class FluidBuilder<R extends AbstractVisceralRegistrar<R>> implements
        TaggableBuilder<Fluid>, ICreativeTabOutputs {

    private static final List<FluidBuilder<?>> ALL_BUILDERS = new ArrayList<>();

    static {CreativeTabBuilderRegistry.registerBuilderProvider(FluidBuilder::getAllBuilders);}

    private final R registrar;
    private final String name;

    private String langEntry;

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();
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
    private Consumer<ItemBuilder<VisceralBucketItem, R>> bucketSetup = builder -> {};

    private boolean registerBlock = false;
    private boolean registerBucket = false;

    public FluidBuilder(R registrar, String name) {
        this.registrar = registrar;
        this.name = name;
        ALL_BUILDERS.add(this);
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

    public FluidBuilder<R> tab(Supplier<ResourceKey<CreativeModeTab>> tab) {
        creativeTabs.add(tab);
        return this;
    }

    public FluidBuilder<R> tab(ResourceKey<CreativeModeTab> tab) {
        creativeTabs.add(() -> tab);
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
        this.blockBuilder = new BlockBuilder<>(registrar, name, props -> null);
        blockSetup.accept(this.blockBuilder);
        return this;
    }

    public FluidBuilder<R> withBucket(Consumer<ItemBuilder<VisceralBucketItem, R>> setup) {
        this.registerBucket = true;
        this.bucketSetup = setup;
        return this;
    }

    public FluidBuilder<R> withBucket() {
        return withBucket(builder -> {});
    }

    public FluidEntry<VisceralFlowingFluid.Flowing> register() {
        if (stillTexture == null) stillTexture = VisceraLib.path(registrar.getModId(), ("fluid/" + name + "_still"));
        if (flowTexture == null) flowTexture = VisceraLib.path(registrar.getModId(), ("fluid/" + name + "_flow"));
        if (overlayTexture == null) overlayTexture = VisceraLib.path(registrar.getModId(), ("gui/" + name + "_overlay"));

        VisceralDeferredRegister<Fluid> fluidRegister = registrar.deferredRegister(Registries.FLUID);

        ResourceLocation stillLoc = VisceraLib.path(registrar.getModId(), name);
        ResourceLocation flowingLoc = VisceraLib.path(registrar.getModId(), "flowing_" + name);

        ResourceKey<VisceralFlowingFluid.Source> stillKey =
                (ResourceKey<VisceralFlowingFluid.Source>) (ResourceKey<?>) ResourceKey.create(Registries.FLUID, stillLoc);
        ResourceKey<VisceralFlowingFluid.Flowing> flowingKey =
                (ResourceKey<VisceralFlowingFluid.Flowing>) (ResourceKey<?>) ResourceKey.create(Registries.FLUID, flowingLoc);

        if (registerBucket) {
            this.bucketBuilder = new ItemBuilder<>(registrar, name + "_bucket",
                    props -> new VisceralBucketItem(() -> stillFluidSupplier.get(), props));
            bucketSetup.accept(this.bucketBuilder);

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
                        () -> flowingFluidSupplier.get())
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
            blockBuilder.blockSupplier(() ->
                    new VisceralLiquidBlock(stillFluidSupplier.get(), blockBuilder.getProperties())
            );
            blockBuilder.register();
        }

        return new FluidEntry<>(flowingFluidSupplier);
    }

    @Override
    public List<TagKey<Fluid>> getTags() {
        return List.copyOf(fluidTags);
    }

    public Optional<Supplier<Item>> getBucketItemSupplier() {
        return bucketBuilder != null
                ? bucketBuilder.getRegisteredSupplier().map(supplier -> (Supplier<Item>) supplier)
                : Optional.empty();
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langEntry);
    }

    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return creativeTabs.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Collection<ItemStack> getTabContents() {
        return getBucketItemSupplier()
                .map(supplier -> List.of(new ItemStack(supplier.get())))
                .orElse(Collections.emptyList());
    }

    public static List<FluidBuilder<?>> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    public static void provideLang(VisceralLangProvider provider) {
        for (FluidBuilder<?> builder : getAllBuilders()) {
            var flowingSupplier = builder.flowingFluidSupplier;
            var stillSupplier = builder.stillFluidSupplier;

            if (flowingSupplier == null || stillSupplier == null)
                continue;

            var fluid = flowingSupplier.get();
            var id = BuiltInRegistries.FLUID.getKey(fluid);

            if (!id.getNamespace().equals(provider.getModId()))
                continue;

            String langKey = "fluid." + id.getNamespace() + "." + builder.name;
            String langValue = builder.getLangEntry().orElse(VisceralLangProvider.toEnglishName(builder.name));
            provider.add(langKey, langValue);

            builder.getBucketItemSupplier().ifPresent(bucketSupplier -> {
                Item bucketItem = bucketSupplier.get();
                if (bucketItem != null) {
                    var bucketId = BuiltInRegistries.ITEM.getKey(bucketItem);
                    String bucketLangKey = "item." + bucketId.getNamespace() + "." + bucketId.getPath();
                    String bucketLangValue = langValue + " Bucket";
                    provider.add(bucketLangKey, bucketLangValue);
                }
            });
        }
    }
}
