package net.electrisoma.visceralib.api.registration.registry.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityHolder<BE extends BlockEntity> extends BaseHolder<BlockEntityType<BE>> {

    public BlockEntityHolder(HolderOwner<BlockEntityType<BE>> owner, ResourceKey<BlockEntityType<BE>> key) {
        super(owner, key);
    }

    @Nullable public BE createBlockEntity(BlockPos pos, BlockState state) {
        return value().create(pos, state);
    }

    public boolean is(@Nullable BlockEntity blockEntity) {
        return blockEntity != null && blockEntity.getType() == value();
    }
}