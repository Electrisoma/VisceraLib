package net.electrisoma.visceralib.api.registration.registry.builder;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.CreativeTabHolder;

import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabBuilder extends AbstractBuilder<CreativeModeTab, CreativeModeTab, CreativeTabHolder> {

    private final List<Consumer<Builder>> builderConsumers = new ArrayList<>();
    private Supplier<ItemStack> iconSupplier = () -> ItemStack.EMPTY;
    private final List<Supplier<Item>> itemSuppliers = new ArrayList<>();
    private final List<Supplier<Block>> blockSuppliers = new ArrayList<>();
    private final List<Supplier<ItemStack>> stackSuppliers = new ArrayList<>();
    private final List<Supplier<?>> genericItemSuppliers = new ArrayList<>();

    public CreativeTabBuilder(
            VisceralRegistry owner,
            String name
    ) {
        super(owner, name, BuiltInRegistries.CREATIVE_MODE_TAB);
    }

    public CreativeTabBuilder icon(Block block) {
        this.iconSupplier = () -> new ItemStack(block.asItem());
        return this;
    }

    public CreativeTabBuilder icon(Item item) {
        this.iconSupplier = () -> new ItemStack(item);
        return this;
    }

    public CreativeTabBuilder icon(Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = iconSupplier;
        return this;
    }

    public CreativeTabBuilder add(Supplier<?> objectSupplier) {
        this.genericItemSuppliers.add(objectSupplier);
        return this;
    }

    public CreativeTabBuilder add(Item item) {
        itemSuppliers.add(() -> item);
        return this;
    }

    public CreativeTabBuilder add(Block block) {
        blockSuppliers.add(() -> block);
        return this;
    }

    public CreativeTabBuilder addItem(Supplier<? extends Item> itemSupplier) {
        itemSuppliers.add(itemSupplier::get);
        return this;
    }

    public CreativeTabBuilder addBlock(Supplier<? extends Block> blockSupplier) {
        blockSuppliers.add(blockSupplier::get);
        return this;
    }

    public CreativeTabBuilder addStack(Supplier<ItemStack> stackSupplier) {
        stackSuppliers.add(stackSupplier);
        return this;
    }

    public CreativeTabBuilder contents(Consumer<Output> contentsConsumer) {
        builderConsumers.add(builder -> {
            CreativeModeTab.DisplayItemsGenerator generator = (itemDisplayParameters, output) ->
                    contentsConsumer.accept(output);

            builder.displayItems(generator);
        });

        return this;
    }

    @Override
    CreativeModeTab build() {
        Builder builder = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1);
        builder.icon(iconSupplier);

        CreativeModeTab.DisplayItemsGenerator generator = (itemDisplayParameters, output) -> {
            for (Supplier<?> supplier : genericItemSuppliers) {
                Object obj = supplier.get();
                ItemStack stack = ItemStack.EMPTY;

                switch (obj) {
                    case Block block -> stack = new ItemStack(block.asItem());
                    case Item item -> stack = new ItemStack(item);
                    case null, default -> {}
                }

                if (!stack.isEmpty())
                    output.accept(stack);
            }

            stackSuppliers.forEach(supplier -> output.accept(supplier.get()));
        };

        builder.displayItems(generator);
        for (Consumer<Builder> consumer : builderConsumers)
            consumer.accept(builder);

        return builder.title(Component.translatable("itemGroup." + id.getNamespace() + "." + id.getPath()))
                .build();
    }

    @Override
    CreativeTabHolder getHolder(HolderOwner<CreativeModeTab> owner, ResourceKey<CreativeModeTab> key) {
        return new CreativeTabHolder(owner, key);
    }
}