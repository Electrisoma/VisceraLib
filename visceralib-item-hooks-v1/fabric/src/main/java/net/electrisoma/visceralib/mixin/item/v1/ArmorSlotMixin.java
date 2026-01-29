package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.EquipmentHook;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorSlot.class)
public abstract class ArmorSlotMixin {

    @Shadow @Final private EquipmentSlot slot;
    @Shadow @Final private LivingEntity owner;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void viscera$bridgeCanEquipUI(
            ItemStack stack,
            CallbackInfoReturnable<Boolean> cir
    ) {
        EquipmentHook hook = stack.getItem();
        if (hook.viscera$canFitSlot(stack, this.slot, this.owner)) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }
}