package net.electrisoma.visceralib.registry.test;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestBlockEntity extends BlockEntity {

    public TestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(TestBlockEntityRegistry.TEST_BLOCK_ENTITY.get(), pos, state);
    }

    public void incrementCount() {
        if (this.level != null && !this.level.isClientSide) {
            System.out.println("Counter Block Entity at " + this.worldPosition + " was clicked!");
        }
    }
}
