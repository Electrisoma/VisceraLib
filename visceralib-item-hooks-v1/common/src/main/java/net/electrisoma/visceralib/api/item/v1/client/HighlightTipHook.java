package net.electrisoma.visceralib.api.item.v1.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface HighlightTipHook {

    /**
     * Modifies the text displayed above the hotbar when the item is selected.
     *
     * @param stack       the item stack being highlighted
     * @param displayName the current name (including custom names/formatting)
     * @return The component to display
     */
    default Component viscera$getHighlightedName(
            ItemStack stack,
            Component displayName
    ) {
        return displayName;
    }
}
