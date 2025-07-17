package net.electrisoma.visceralib.testreg.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            return InteractionResultHolder.pass(stack);
        }

        for (int i = 0; i < 10; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 0.5 + 1.0;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;

            level.addParticle(
                    ParticleTypes.CLOUD,
                    player.getX() + offsetX,
                    player.getY() + offsetY,
                    player.getZ() + offsetZ,
                    0, 0.05, 0
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
