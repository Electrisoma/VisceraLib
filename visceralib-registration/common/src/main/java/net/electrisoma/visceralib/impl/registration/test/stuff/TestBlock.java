package net.electrisoma.visceralib.impl.registration.test.stuff;

import com.mojang.serialization.MapCodec;
import net.electrisoma.visceralib.impl.registration.test.TestBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestBlock extends BaseEntityBlock {

    public TestBlock(Properties properties) {
        super(properties);
    }

    /*? >=1.21.1 {*/
    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
    /*?}*/

    @Override
    @NotNull
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TestBlockEntity(pos, state);
    }

    /*? >=1.21.1 {*/
    @Override
    @NotNull
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof TestBlockEntity counterEntity) {
            counterEntity.incrementCount();
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
    /*?}*/

    /*? =1.20.1 {*/
    /*@Override @NotNull
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof TestBlockEntity counterEntity) {
            counterEntity.incrementCount();
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
    *//*?}*/

    public static class TestBlockItem extends BlockItem {
        public TestBlockItem(Properties properties) {
            super(TestBlocks.BLOCK.get(), properties);
        }
    }
}