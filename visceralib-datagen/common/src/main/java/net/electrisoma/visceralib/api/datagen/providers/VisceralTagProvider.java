package net.electrisoma.visceralib.api.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.Registry;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class VisceralTagProvider<T> extends TagsProvider<T> {

    private final String modid;

    protected VisceralTagProvider(
            PackOutput output,
            ResourceKey<? extends Registry<T>> registryKey,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            String modid
    ) {
        super(output, registryKey, lookupProvider);
        this.modid = modid;
    }

    protected abstract void addTags(HolderLookup.Provider lookup);

    protected abstract ResourceKey<T> reverseLookup(T element);

    @Override @NotNull
    protected VisceralTagBuilder<T> tag(@NotNull TagKey<T> tag) {
        return new VisceralTagBuilder<>(getOrCreateRawBuilder(tag), this::reverseLookup);
    }

    @NotNull
    public String visceral$getName() {
        return "Visceral Tag Provider (" + registryKey.location() + ") for " + modid;
    }

    public static class VisceralTagBuilder<T> extends TagAppender<T> {

        private final TagBuilder internalBuilder;
        private final Function<T, ResourceKey<T>> lookup;

        private VisceralTagBuilder(
                TagBuilder builder,
                Function<T, ResourceKey<T>> lookup
        ) {
            super(builder);
            this.internalBuilder = builder;
            this.lookup = lookup;
        }

        public VisceralTagBuilder<T> add(T element) {
            this.internalBuilder.addElement(lookup.apply(element).location());
            return this;
        }

        @SafeVarargs
        public final VisceralTagBuilder<T> add(T... elements) {
            Arrays.stream(elements).forEach(this::add);
            return this;
        }

        @NotNull
        public VisceralTagBuilder<T> visceral$add(@NotNull ResourceKey<T> key) {
            super.add(key);
            return this;
        }

        @SafeVarargs
        public final VisceralTagBuilder<T> visceral$add(@NotNull ResourceKey<T>... keys) {
            for (ResourceKey<T> key : keys)
                this.visceral$add(key);
            return this;
        }

        @Override @NotNull
        public VisceralTagBuilder<T> addTag(@NotNull TagKey<T> tag) {
            super.addTag(tag);
            return this;
        }

        @SafeVarargs
        public final void addTags(@NotNull TagKey<T>... tags) {
            for (TagKey<T> tag : tags)
                this.addTag(tag);
        }

        @Override @NotNull
        public VisceralTagBuilder<T> addOptional(@NotNull ResourceLocation id) {
            super.addOptional(id);
            return this;
        }

        @Override @NotNull
        public VisceralTagBuilder<T> addOptionalTag(@NotNull ResourceLocation id) {
            super.addOptionalTag(id);
            return this;
        }
    }

    public abstract static class BlockTagProvider extends VisceralTagProvider<Block> {

        public BlockTagProvider(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> lookupProvider,
                String modid
        ) {
            super(output, Registries.BLOCK, lookupProvider, modid);
        }

        @Override
        protected ResourceKey<Block> reverseLookup(Block element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class ItemTagProvider extends VisceralTagProvider<Item> {

        public ItemTagProvider(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> lookupProvider,
                String modid
        ) {
            super(output, Registries.ITEM, lookupProvider, modid);
        }

        @Override
        protected ResourceKey<Item> reverseLookup(Item element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class FluidTagProvider extends VisceralTagProvider<Fluid> {

        public FluidTagProvider(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> lookupProvider,
                String modid
        ) {
            super(output, Registries.FLUID, lookupProvider, modid);
        }

        @Override
        protected ResourceKey<Fluid> reverseLookup(Fluid element) {
            return element.builtInRegistryHolder().key();
        }
    }

    public abstract static class EntityTypeTagProvider extends VisceralTagProvider<EntityType<?>> {

        public EntityTypeTagProvider(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> lookupProvider,
                String modid
        ) {
            super(output, Registries.ENTITY_TYPE, lookupProvider, modid);
        }

        @Override
        protected ResourceKey<EntityType<?>> reverseLookup(EntityType<?> element) {
            return element.builtInRegistryHolder().key();
        }
    }
}