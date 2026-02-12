package net.electrisoma.visceralib.api.item.v1.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ArmPoseHook {

    /**
     * Returns a custom arm pose for the given player holding this item.
     *
     * @param player the player holding the item
     * @param hand   the hand holding the item
     * @param stack  the item stack
     * @return the custom arm pose, or null to use vanilla logic
     */
    @Nullable
    default HumanoidModel.ArmPose viscera$getArmPose(
            ItemStack stack,
            AbstractClientPlayer player,
            InteractionHand hand
    ) {
        return null;
    }
}