package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.BlockEntityEntry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity, R extends AbstractVisceralRegistrar<R>>
        extends AbstractBuilder<BlockEntityType<T>, R, BlockEntityBuilder<T, R>> {

    private final VisceralDeferredRegister<BlockEntityType<?>> register;
    private VisceralRegistrySupplier<BlockEntityType<T>> registeredSupplier;

    private final BlockEntityType.BlockEntitySupplier<T> constructor;

    private final List<Supplier<? extends Block>> validBlocks = new ArrayList<>();

    public BlockEntityBuilder(R registrar, String name, BlockEntityType.BlockEntitySupplier<T> constructor) {
        super(registrar, name);
        this.constructor = constructor;
        this.register = registrar.deferredRegister(Registries.BLOCK_ENTITY_TYPE);
    }

    public BlockEntityBuilder<T, R> validBlock(Supplier<? extends Block> block) {
        this.validBlocks.clear();
        this.validBlocks.add(block);
        return self();
    }

    public BlockEntityBuilder<T, R> validBlocks(List<Supplier<? extends Block>> blocks) {
        this.validBlocks.clear();
        this.validBlocks.addAll(blocks);
        return self();
    }

    @SafeVarargs
    public final BlockEntityBuilder<T, R> validBlocks(Supplier<? extends Block>... blocks) {
        this.validBlocks.clear();
        Collections.addAll(this.validBlocks, blocks);
        return self();
    }

    public BlockEntityBuilder<T, R> addValidBlock(Supplier<? extends Block> block) {
        this.validBlocks.add(block);
        return self();
    }

    @SuppressWarnings("unchecked")
    public BlockEntityEntry<T> register() {
        if (validBlocks.isEmpty())
            throw new IllegalStateException("Block entity must have at least one valid block!");

        VisceralRegistrySupplier<BlockEntityType<?>> raw = register.register(name, () ->
                BlockEntityType.Builder.of(constructor, validBlocks.stream().map(Supplier::get).toArray(Block[]::new))
                        .build(null)
        );

        VisceralRegistrySupplier<BlockEntityType<T>> typed = new VisceralRegistrySupplier<>(
                (ResourceKey<BlockEntityType<T>>) (ResourceKey<?>) raw.getKey(),
                () -> (BlockEntityType<T>) raw.get()
        );

        typed.listen(be -> postRegisterTasks.forEach(task -> task.accept(be)));
        this.registeredSupplier = typed;

        return new BlockEntityEntry<>(typed);
    }

    @Override
    public Optional<VisceralRegistrySupplier<BlockEntityType<T>>> getRegisteredSupplier() {
        return Optional.ofNullable(registeredSupplier);
    }
}