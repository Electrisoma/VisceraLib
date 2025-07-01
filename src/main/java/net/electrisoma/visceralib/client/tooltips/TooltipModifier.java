package net.electrisoma.visceralib.client.tooltips;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public interface TooltipModifier {
    Map<Item, TooltipModifier> REGISTRY = new IdentityHashMap<>();

    void modify(ItemStack stack, List<Component> tooltip);

    static void register(Item item, TooltipModifier modifier) {
        REGISTRY.put(item, modifier);
    }

    static TooltipModifier get(Item item) {
        return REGISTRY.getOrDefault(item, EMPTY);
    }

    TooltipModifier EMPTY = (stack, tooltip) -> {};

    TooltipModifier DEFAULT = (stack, tooltip) -> {
        String baseKey = stack.getItem().getDescriptionId() + ".tooltip";
        ItemDescription desc = ItemDescription.fromTranslationKey(baseKey);
        tooltip.addAll(desc.getTooltipLines());
    };
}