package net.electrisoma.resotech.api.registration.builders;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.electrisoma.resotech.api.registration.fluid.ResoLiquidBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.electrisoma.resotech.ResoTech.path;

@SuppressWarnings({"unused"})
public class FluidBuilder {
    private static final List<FluidBuilder> ALL_BUILDERS = new ArrayList<>();
    public static List<FluidBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private static final List<ArchitecturyFluidAttributes> ALL_FLUID_ATTRIBUTES = new ArrayList<>();
    public static List<ArchitecturyFluidAttributes> getAllAttributes() {
        return Collections.unmodifiableList(ALL_FLUID_ATTRIBUTES);
    }

    private static final Map<Block, FluidBuilder> BLOCK_TO_BUILDER = new HashMap<>();

    public static Optional<FluidBuilder> fromBlock(Block block) {
        return Optional.ofNullable(BLOCK_TO_BUILDER.get(block));
    }

    private final String name;
    private String langEntry = null;
    private final List<TagKey<Fluid>> fluidTags = new ArrayList<>();

    private final DeferredRegister<Fluid> fluidRegister;
    private final DeferredRegister<Item> itemRegister;
    private final DeferredRegister<Block> blockRegister;

    private RegistrySupplier<FlowingFluid> flowing;
    private RegistrySupplier<FlowingFluid> still;
    private RegistrySupplier<LiquidBlock> block;
    private RegistrySupplier<Item> bucket;

    private ArchitecturyFluidAttributes attributes;
    private ResourceLocation stillTexture;
    private ResourceLocation flowTexture;
    //private ResourceLocation overlayTexture;
    private int color = 0xFFFFFFFF;
    private int luminance = 0;
    public int density = 1000;
    private int temperature = 300;
    private int viscosity = 1000;
    private int slopeFindDistance = 4;
    private int dropOff = 1;
    private int tickDelay = 5;
    private float explosionResistance = 100.0F;
    private boolean convertToSource = false;
    private boolean lighterThanAir = false;
    private static float fogDensity;

    private Consumer<Item.Properties> itemProps = p -> {};
    private Consumer<BlockBehaviour.Properties> blockProps = p -> {};

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();

    public FluidBuilder lang(String langEntry) {
        this.langEntry = langEntry;
        return this;
    }
    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langEntry);
    }

    public FluidBuilder(String name, DeferredRegister<Fluid> fluidRegister,
                        DeferredRegister<Item> itemRegister,
                        DeferredRegister<Block> blockRegister) {
        this.name = name;
        this.fluidRegister = fluidRegister;
        this.itemRegister = itemRegister;
        this.blockRegister = blockRegister;
        ALL_BUILDERS.add(this);
    }

    private Vec3 fogColor = new Vec3(1.0, 1.0, 1.0);
    public FluidBuilder fogColor(Vec3 color) {
        this.fogColor = color;
        return this;
    }
    public Vec3 getFogColor() {
        return fogColor;
    }
    public FluidBuilder fogDensity(float fogDensity) {
        FluidBuilder.fogDensity = fogDensity;
        return this;
    }
    public static float getFogDensity() {
        return fogDensity;
    }

    public FluidBuilder textures(ResourceLocation still, ResourceLocation flowing) {
        this.stillTexture = still;
        this.flowTexture = flowing;
        return this;
    }
    public FluidBuilder color(int color) {
        this.color = color;
        return this;
    }
    public FluidBuilder luminance(int value) {
        this.luminance = value;
        return this;
    }
    public FluidBuilder density(int value) {
        this.density = value;
        return this;
    }
    public FluidBuilder temperature(int value) {
        this.temperature = value;
        return this;
    }
    public FluidBuilder viscosity(int value) {
        this.viscosity = value;
        return this;
    }
    public FluidBuilder slopeFindDistance(int value) {
        this.slopeFindDistance = value;
        return this;
    }
    public FluidBuilder dropOff(int value) {
        this.dropOff = value;
        return this;
    }
    public FluidBuilder tickDelay(int value) {
        this.tickDelay = value;
        return this;
    }
    public FluidBuilder explosionResistance(float value) {
        this.explosionResistance = value;
        return this;
    }
    public FluidBuilder convertToSource(boolean value) {
        this.convertToSource = value;
        return this;
    }
    public FluidBuilder lighterThanAir(boolean value) {
        this.lighterThanAir = value;
        return this;
    }

    public FluidBuilder itemProperties(Consumer<Item.Properties> props) {
        this.itemProps = props;
        return this;
    }
    public FluidBuilder blockProperties(Consumer<BlockBehaviour.Properties> props) {
        this.blockProps = props;
        return this;
    }

    public FluidBuilder tab(Supplier<ResourceKey<CreativeModeTab>> tabKeySupplier) {
        creativeTabs.add(tabKeySupplier);
        return this;
    }
    public FluidBuilder tab(RegistrySupplier<CreativeModeTab> tabSupplier) {
        Objects.requireNonNull(tabSupplier, "Creative tab supplier cannot be null");
        creativeTabs.add(() -> {
            var id = tabSupplier.getId();
            if (id == null) {
                throw new IllegalStateException("Creative tab ID is null. Is it being registered too late?");
            }
            return ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        });
        return this;
    }
    public Set<ResourceKey<CreativeModeTab>> getTabs() {
        return creativeTabs.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet());
    }

    public FluidBuilder tag(TagKey<Fluid> tag) {
        this.fluidTags.add(tag);
        return this;
    }
    @SafeVarargs public final FluidBuilder tags(TagKey<Fluid>... tags) {
        Collections.addAll(this.fluidTags, tags);
        return this;
    }
    public List<TagKey<Fluid>> getFluidTags() {
        return Collections.unmodifiableList(fluidTags);
    }

    public RegistrySupplier<FlowingFluid> register() {
        if (stillTexture == null) {
            stillTexture = path("fluid/" + name + "_still");
        }
        if (flowTexture == null) {
            flowTexture = path("fluid/" + name + "_flow");
        }
//        if (overlayTexture == null) {
//            flowTexture = path("gui/" + name + "_overlay");
//        }

        flowing = fluidRegister.register("flowing_" + name,
                () -> new ArchitecturyFlowingFluid.Flowing(attributes));

        still = fluidRegister.register(name,
                () -> new ArchitecturyFlowingFluid.Source(attributes));

        attributes = SimpleArchitecturyFluidAttributes.ofSupplier(() -> flowing, () -> still)
                .blockSupplier(() -> block)
                .bucketItemSupplier(() -> bucket)
                .flowingTexture(flowTexture)
                .sourceTexture(stillTexture)
                //.overlayTexture(overlayTexture)
                .color(color)
                .luminosity(luminance)
                .density(density)
                .temperature(temperature)
                .viscosity(viscosity)
                .slopeFindDistance(slopeFindDistance)
                .dropOff(dropOff)
                .tickDelay(tickDelay)
                .explosionResistance(explosionResistance)
                .convertToSource(convertToSource)
                .lighterThanAir(lighterThanAir)
        ;

        block = blockRegister.register(name, () -> {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.WATER);
            blockProps.accept(props);
            ResoLiquidBlock liquidBlock = new ResoLiquidBlock(still, props);
            BLOCK_TO_BUILDER.put(liquidBlock, this);
            return liquidBlock;
        });

        bucket = itemRegister.register(name + "_bucket", () -> {
            Item.Properties props = new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1);

            for (Supplier<ResourceKey<CreativeModeTab>> tabSupplier : creativeTabs) {
                //noinspection UnstableApiUsage
                props = props.arch$tab(tabSupplier.get());
            }

            itemProps.accept(props);
            return new ArchitecturyBucketItem(still, props);
        });

        ALL_FLUID_ATTRIBUTES.add(attributes);

        return still;
    }

    public RegistrySupplier<FlowingFluid> getFlowing() {
        return flowing;
    }
    public RegistrySupplier<FlowingFluid> getStill() {
        return still;
    }
    public RegistrySupplier<LiquidBlock> getBlock() {
        return block;
    }
    public RegistrySupplier<Item> getBucket() {
        return bucket;
    }
    public ArchitecturyFluidAttributes getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }
}
