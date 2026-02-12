package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelReader;

public interface UseHook {

    /**
     * Called before a block is activated when the player right click while holding this item.
     * <p>
     * Use this to intercept or prioritize item logic over block logic.
     *
     * @param stack   the item stack being used
     * @param context the context of the interaction, including position and player data
     * @return success to consume the action,
     * fail to cancel, or
     * pass to allow normal block interaction logic.
     */
    default InteractionResult viscera$onPreBlockInteraction(
            ItemStack stack,
            UseOnContext context
    ) {
        return InteractionResult.PASS;
    }

    /**
     * A universal cleanup hook triggered whenever item usage ends.
     * <p>
     * Unlike {@code finishUsingItem} or {@code releaseUsing},
     * this triggers regardless of <i>how</i> the usage stopped.
     *
     * @param stack  the item being held.
     * @param entity the entity holding the item; usually the player.
     * @param count  total ticks the item was held in use before stopping.
     */
    default void viscera$onPostBlockInteraction(
            ItemStack stack,
            LivingEntity entity,
            int count
    ) {
        // no-op
    }

    /**
     * Determines if this item should ignore a block's sneak bypass behavior.
     * <p>
     * By default, sneaking allows a player to place a block against a container
     * without opening its UI. Returning true allows this item's interaction
     * to take priority even while the player is sneaking.
     *
     * @param stack  the item stack being used
     * @param level  the world level where the interaction occurs
     * @param pos    the position of the block being targeted
     * @param player the player performing the interaction
     * @return true to bypass sneaking logic, false for default behavior
     */
    default boolean viscera$shouldBypassSneak(
            ItemStack stack,
            LevelReader level,
            BlockPos pos,
            Player player
    ) {
        return false;
    }
}
