package net.electrisoma.visceralib.api.item.v1;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface EntityHook {

    default boolean viscera$onEntityAttack(
            ItemStack stack,
            Player player,
            Entity entity
    ) {
        return false;
    }

    default int viscera$getEntityDespawn(
            ItemStack stack,
            Level level
    ) {
        return 6000;
    }

    default boolean viscera$customItemEntity(ItemStack stack) {
        return false;
    }

    @Nullable
    default Entity viscera$createEntity(
            Level level,
            Entity location,
            ItemStack stack
    ) {
        return null;
    }

    default boolean viscera$onEntityItemUpdate(
            ItemStack stack,
            ItemEntity entity
    ) {
        return false;
    }
}
