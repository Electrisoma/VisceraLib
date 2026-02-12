package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface DropHook {

    /**
     * Called when a player drops the item into the world.
     *
     * @param stack  the item stack being dropped
     * @param player the player dropping the item
     * @return true to allow the drop, false to cancel it
     */
    default boolean viscera$canPlayerDrop(
            ItemStack stack,
            Player player
    ) {
        return true;
    }
}
