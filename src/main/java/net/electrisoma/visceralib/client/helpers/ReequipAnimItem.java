package net.electrisoma.visceralib.client.helpers;

import net.minecraft.world.item.ItemStack;

public interface ReequipAnimItem {
    boolean shouldUseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged);
}
