package net.electrisoma.visceralib.api.registration.registry.builder;

import net.electrisoma.visceralib.api.registration.registry.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.registry.holder.BlockEntityHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockEntityBuilder<BE extends BlockEntity> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<BE>, BlockEntityHolder<BE>> {

    private final BlockEntityFactory<BE> factory;
    private final Set<Supplier<@NotNull Block>> validBlocks = new HashSet<>();

    public BlockEntityBuilder(
            VisceralRegistry owner,
            String name,
            BlockEntityFactory<BE> factory
    ) {
        super(owner, name, BuiltInRegistries.BLOCK_ENTITY_TYPE);
        this.factory = factory;
    }

    @SafeVarargs
    public final BlockEntityBuilder<BE> validBlocks(Supplier<@NotNull Block>... blocks) {
        return validBlocks(List.of(blocks));
    }

    public BlockEntityBuilder<BE> validBlocks(List<Supplier<@NotNull Block>> blocks) {
        validBlocks.addAll(blocks);
        return this;
    }

    @Override
    BlockEntityType<BE> build() {
        Set<Block> validBlocks = this.validBlocks.stream()
                .map(Supplier::get)
                .collect(Collectors.toSet());
        //noinspection DataFlowIssue
        return new BlockEntityType<>((p, s) -> factory.createBlockEntity(holder.value(), p, s), validBlocks, null);
    }

    @Override
    BlockEntityHolder<BE> getHolder(HolderOwner<BlockEntityType<?>> owner, ResourceKey<BlockEntityType<?>> key) {
        //noinspection unchecked, rawtypes
        return new BlockEntityHolder<>((HolderOwner) owner, (ResourceKey) key);
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T createBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state);
    }
}