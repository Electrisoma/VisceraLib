package net.electrisoma.resotech.api.registration;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.electrisoma.resotech.ResoTech;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.electrisoma.resotech.ResoTech.path;

public class FluidBuilder {
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
    private int color = 0xFFFFFFFF;
    private boolean convertToSource = false;
    private boolean lighterThanAir = false;

    private Consumer<Item.Properties> itemProps = p -> {};
    private Consumer<BlockBehaviour.Properties> blockProps = p -> {};

    private final List<ArchitecturyFluidAttributes> fluidAttributesList;

    private final Set<Supplier<ResourceKey<CreativeModeTab>>> creativeTabs = new HashSet<>();

    public FluidBuilder lang(String langEntry) {
        this.langEntry = langEntry;
        return this;
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langEntry);
    }

    private static final List<FluidBuilder> ALL_BUILDERS = new ArrayList<>();

    public static List<FluidBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    public FluidBuilder(String name, DeferredRegister<Fluid> fluidRegister,
                        DeferredRegister<Item> itemRegister,
                        DeferredRegister<Block> blockRegister,
                        List<ArchitecturyFluidAttributes> fluidAttributesList) {
        this.name = name;
        this.fluidRegister = fluidRegister;
        this.itemRegister = itemRegister;
        this.blockRegister = blockRegister;
        this.fluidAttributesList = fluidAttributesList;
        ALL_BUILDERS.add(this);
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

    @SafeVarargs
    public final FluidBuilder tags(TagKey<Fluid>... tags) {
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

        flowing = fluidRegister.register("flowing_" + name,
                () -> new ArchitecturyFlowingFluid.Flowing(attributes));

        still = fluidRegister.register(name,
                () -> new ArchitecturyFlowingFluid.Source(attributes));

        attributes = SimpleArchitecturyFluidAttributes.ofSupplier(() -> flowing, () -> still)
                .blockSupplier(() -> block)
                .bucketItemSupplier(() -> bucket)
                .flowingTexture(flowTexture)
                .sourceTexture(stillTexture)
                .color(color)
                .convertToSource(convertToSource)
                .lighterThanAir(lighterThanAir);

        block = blockRegister.register(name, () -> {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.WATER);
            blockProps.accept(props);
            return new ArchitecturyLiquidBlock(still, props);
        });

        bucket = itemRegister.register(name + "_bucket", () -> {
            Item.Properties props = new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1);

            for (Supplier<ResourceKey<CreativeModeTab>> tabSupplier : creativeTabs) {
                props = props.arch$tab(tabSupplier.get());
            }

            itemProps.accept(props);
            return new ArchitecturyBucketItem(still, props);
        });

        fluidAttributesList.add(attributes);

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
