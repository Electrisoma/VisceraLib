package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface CraftingRemainderHook {

    default ItemStack viscera$getCraftingRemaining(ItemStack stack) {
        return Optional.ofNullable(stack.getItem().getCraftingRemainingItem())
                .map(Item::getDefaultInstance)
                .orElse(ItemStack.EMPTY);
    }
}
