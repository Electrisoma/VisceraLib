package net.electrisoma.visceralib.mixin.client;

import net.electrisoma.visceralib.client.helpers.ReequipAnimItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class ReequipAnimationMixin {
    @Unique private ItemStack viscera$mainHand;
    @Unique private ItemStack viscera$offHand;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"))
    private void modifyProgressAnimation(CallbackInfo ci) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        ItemStack newMainStack = player.getMainHandItem();
        ItemStack newOffStack = player.getOffhandItem();

        if (viscera$mainHand == null) viscera$mainHand = newMainStack;
        if (viscera$offHand == null) viscera$offHand = newOffStack;

        if (viscera$mainHand.getItem() instanceof ReequipAnimItem animItem) {
            boolean slotChanged = false;
            if (!animItem.shouldUseReequipAnimation(viscera$mainHand, newMainStack, slotChanged))
                viscera$mainHand = newMainStack;
        }

        if (viscera$offHand.getItem() instanceof ReequipAnimItem animItem) {
            boolean slotChanged = false;
            if (!animItem.shouldUseReequipAnimation(viscera$offHand, newOffStack, slotChanged))
                viscera$offHand = newOffStack;
        }
    }
}
