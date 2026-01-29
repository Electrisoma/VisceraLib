package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.DurabilityHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class NeoForgeItemMixin {

    @Inject(
            method = "isRepairable",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void viscera$bridgeIsRepairable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof DurabilityHook hook) {
            cir.setReturnValue(hook.viscera$canBeRepaired(stack));
        }
    }
}