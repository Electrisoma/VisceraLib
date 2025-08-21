package net.electrisoma.visceralib.testreg.block_entities;

import net.electrisoma.visceralib.testreg.VisceralibBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TestBlockEntity extends BlockEntity {
    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(VisceralibBlockEntities.MY_BLOCK_ENTITY.get(), pos, state);
    }
}
