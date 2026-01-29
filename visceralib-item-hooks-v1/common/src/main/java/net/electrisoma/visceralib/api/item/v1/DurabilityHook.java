package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface DurabilityHook {

    default int viscera$getDamageValue(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(DataComponents.DAMAGE, 0), 0, stack.getMaxDamage());
    }

    default int viscera$getMaxDurability(ItemStack stack) {
        return stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
    }

    default boolean viscera$hasWear(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    default void viscera$setDamageValue(
            ItemStack stack,
            int damage
    ) {
        stack.set(DataComponents.DAMAGE, Mth.clamp(damage, 0, stack.getMaxDamage()));
    }

    default int viscera$onCalculateDamage(
            ItemStack stack,
            int amount,
            @Nullable LivingEntity entity
    ) {
        return amount;
    }

    default boolean viscera$canTakeDamage(ItemStack stack) {
        return stack.has(DataComponents.MAX_DAMAGE) &&
                !stack.has(DataComponents.UNBREAKABLE) &&
                stack.has(DataComponents.DAMAGE);
    }

    default boolean viscera$canBeRepaired(ItemStack stack) {
        return stack.has(DataComponents.MAX_DAMAGE);
    }

    default float viscera$getMendingEfficiency(ItemStack stack) {
        return 1.0f;
    }
}
