package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public interface EnchantmentHook {

    default boolean viscera$isEnchantableWithBook(
            ItemStack stack,
            ItemStack book
    ) {
        return true;
    }

    default int viscera$getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantment);
    }

    default ItemEnchantments viscera$getAllEnchantments(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
    }

    default int viscera$getEnchantmentValue(ItemStack stack) {
        return stack.getItem().getEnchantmentValue();
    }

    default boolean viscera$isCompatibleWithEnchantment(
            ItemStack stack,
            Holder<Enchantment> enchantment,
            boolean isPrimary
    ) {
        if (!isPrimary)
            return stack.is(Items.ENCHANTED_BOOK) || enchantment.value().isSupportedItem(stack);
        if (stack.is(Items.BOOK)) return true;
        return viscera$isCompatibleWithEnchantment(stack, enchantment, false) &&
                enchantment.value().definition().primaryItems().map(stack::is).orElse(true);
    }
}
