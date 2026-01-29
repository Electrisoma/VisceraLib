package net.electrisoma.visceralib.impl.registration.v1.test;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public final class TestPickItem extends PickaxeItem {

    public TestPickItem(Properties properties) {
        super(Tiers.NETHERITE,  properties);
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            Level level,
            Entity entity,
            int slotId,
            boolean isSelected
    ) {
        if (!level.isClientSide && isSelected && entity instanceof Player player) {
            if (level.getGameTime() % 20 == 0)
                toggleState(stack, player, level);
        }
    }

    private void toggleState(ItemStack stack, Player player, Level level) {
        long time = level.getGameTime();
        Component customName = Component.literal("Test " + time).withStyle(ChatFormatting.GOLD);

        if (stack.has(DataComponents.ITEM_NAME)) {
            stack.remove(DataComponents.ITEM_NAME);
        } else {
            stack.set(DataComponents.ITEM_NAME, customName);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.2F, 2.0F);
    }

    @Override
    public boolean viscera$canFitSlot(
            ItemStack stack,
            EquipmentSlot slot,
            LivingEntity entity
    ) {
        return true;
    }

    @Override
    public boolean viscera$shouldAllowReequipAnimation(
            ItemStack oldStack,
            ItemStack newStack,
            boolean slotChanged
    ) {
        return false;
    }

    @Override
    public boolean viscera$isBarterCurrency(ItemStack stack) {
        return true;
    }

    @Override
    public boolean viscera$shouldContinueBreaking(
            ItemStack oldStack,
            ItemStack newStack
    ) {
        return true;
    }

    @Override
    public HumanoidModel.ArmPose viscera$getArmPose(
            ItemStack stack,
            AbstractClientPlayer player,
            InteractionHand hand
    ) {
        if (player.isCrouching())
            return HumanoidModel.ArmPose.SPYGLASS;
        return HumanoidModel.ArmPose.THROW_SPEAR;
    }
}