package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockEntityHolder<T extends BlockEntity> extends BaseHolder<BlockEntityType<T>> {
    public BlockEntityHolder(HolderOwner<BlockEntityType<T>> owner, ResourceKey<BlockEntityType<T>> key) {
        super(owner, key);
    }

    @Nullable
    public T createBlockEntity(BlockPos pos, BlockState state) {
        return value().create(pos, state);
    }

    public boolean is(@Nullable BlockEntity blockEntity) {
        return blockEntity != null && blockEntity.getType() == value();
    }

    @Deprecated
    public Optional<T> getOptional(BlockGetter world, BlockPos pos) {
        return Optional.ofNullable(get(world, pos));
    }

    @Nullable
    @Deprecated
    public T get(BlockGetter blockGetter, BlockPos pos) {
        BlockEntity be = blockGetter.getBlockEntity(pos);
        //noinspection unchecked
        return is(be) ? (T) be : null;
    }
}