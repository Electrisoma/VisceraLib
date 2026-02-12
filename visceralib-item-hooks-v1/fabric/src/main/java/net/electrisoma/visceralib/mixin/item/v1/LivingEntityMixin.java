package net.electrisoma.visceralib.mixin.item.v1;

import com.llamalad7.mixinextras.sugar.Local;
import net.electrisoma.visceralib.api.item.v1.CombatHook;
import net.electrisoma.visceralib.api.item.v1.EquipmentHook;
import net.electrisoma.visceralib.api.item.v1.UseHook;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected ItemStack useItem;
    @Shadow protected int useItemRemaining;

    @Inject(method = "stopUsingItem", at = @At("HEAD"))
    private void viscera$handleFabricStopUsing(CallbackInfo ci) {
        if (this.useItem.isEmpty())
            return;
        UseHook hook = this.useItem.getItem();
        hook.viscera$onPostBlockInteraction(this.useItem, (LivingEntity) (Object) this, this.useItemRemaining);
    }

    @Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
    private static void viscera$bridgeEquipmentSlotHook(
            ItemStack stack,
            CallbackInfoReturnable<EquipmentSlot> cir
    ) {
        EquipmentHook hook = stack.getItem();
        EquipmentSlot custom = hook.viscera$getDesiredSlot(stack);
        if (custom != null)
            cir.setReturnValue(custom);
    }

    @Inject(
            method = "hurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;blockUsingShield(Lnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.AFTER)
    )
    private void viscera$bridgeShieldDisable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local LivingEntity attacker) {
        LivingEntity defender = (LivingEntity) (Object) this;
        ItemStack weaponStack = attacker.getMainHandItem();
        ItemStack shieldStack = defender.getUseItem();
        if (weaponStack.getItem() instanceof CombatHook hook) {
            if (hook.viscera$canDisableShield(weaponStack, shieldStack, defender, attacker))
                defender.canDisableShield();
        }
    }
}
