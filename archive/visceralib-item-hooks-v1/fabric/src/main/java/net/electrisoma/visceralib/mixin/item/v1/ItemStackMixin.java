package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void viscera$handleUseFirst(
            UseOnContext context,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof UseHook hook) {
            InteractionResult result = hook.viscera$onPreBlockInteraction(stack, context);
            if (result != InteractionResult.PASS)
                cir.setReturnValue(result);
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void viscera$handleSneakBypass(
            UseOnContext context,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof UseHook hook) {
            Player player = context.getPlayer();
            if (player == null || !player.isSecondaryUseActive())
                return;
            if (hook.viscera$shouldBypassSneak(stack, context.getLevel(), context.getClickedPos(), player))
                cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void viscera$preventIllegalEquip(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.getItem() instanceof EquipmentHook hook) {
            EquipmentSlot target = player.getEquipmentSlotForItem(stack);
            if (target.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                if (!hook.viscera$canFitSlot(stack, target, player))
                    cir.setReturnValue(InteractionResultHolder.fail(stack));
            }
        }
    }

    @Inject(method = "getDamageValue", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeGetDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DurabilityHook hook) {
            cir.setReturnValue(hook.viscera$getDamageValue(stack));
        }
    }

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeGetMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DurabilityHook hook) {
            cir.setReturnValue(hook.viscera$getMaxDurability(stack));
        }
    }

    @Inject(method = "setDamageValue", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeSetDamage(int damage, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DurabilityHook hook) {
            hook.viscera$setDamageValue(stack, damage);
            ci.cancel();
        }
    }

    @Inject(method = "isDamaged", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeIsDamaged(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof DurabilityHook hook) {
            cir.setReturnValue(hook.viscera$hasWear(stack));
        }
    }

    @Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeIsRepairableHook(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this.getItem() instanceof DurabilityHook hook) {
            if (!hook.viscera$canBeRepaired((ItemStack) (Object) this))
                cir.setReturnValue(false);
        }
    }

    @Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeIsDamageableHook(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this.getItem() instanceof DurabilityHook hook) {
            cir.setReturnValue(hook.viscera$canTakeDamage((ItemStack) (Object) this));
        }
    }

    @ModifyVariable(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;" +
                    "Lnet/minecraft/server/level/ServerPlayer;" +
                    "Ljava/util/function/Consumer;" +
                    ")V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private int viscera$bridgeHurtAndBreakHook(
            int value,
            int damage,
            ServerLevel level,
            @Nullable ServerPlayer player,
            Consumer<Item> onBreak
    ) {
        if ((Object) this.getItem() instanceof DurabilityHook hook) {
            return hook.viscera$onCalculateDamage((ItemStack) (Object) this, damage, player);
        }
        return damage;
    }

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.getItem() instanceof StackHook hook) {
            cir.setReturnValue(hook.viscera$getStackLimit(stack));
        }
    }

    @Inject(method = "getEnchantments", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeGetEnchantments(CallbackInfoReturnable<ItemEnchantments> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if (stack.getItem() instanceof EnchantmentHook hook) {
            cir.setReturnValue(hook.viscera$getAllEnchantments(stack));
        }
    }
}